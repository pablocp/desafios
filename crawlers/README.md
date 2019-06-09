Desafio 2: Crawlers
===================
Para este desafio foi solicitada a implementação de um _crawler_ para buscar _threads_ populares do Reddit. O desafio foi dividido em 2 partes, sendo a primeira a criação de uma _CLI_ que seria responsável por receber uma lista de nomes de _subreddits_ separados por ponto-e-vírgula e imprimir a lista de _threads_ com pontuação acima de um limiar dentro desses _subreddits_. A segunda parte é similar à primeira, sendo a interface feita através de um bot do Telegram em vez de linha de comando.

Solução
-------
Uma solução simples para a primeira parte seria criar uma aplicação de console em Java que receberia os comandos e quebraria a lista de _subreddits_  solicitados utilizando o ponto-e-vírgula como delimitador. A partir daí faria uma requisição HTTP para cada _subreddit_, baixando a página correspondente e, após fazer parse da estrutura conhecida dela, separar as informações necessárias e imprimi-las no console. A segunda parte poderia reutilizar parte da implementação da primeira, bastando adicionar um servidor RESTful que funcionaria como _webhook_ para o bot do Telegram.

Esta solução é eficiente apenas para casos de uso mais básicos. Ela não leva em consideração, por exemplo, o alto tempo para baixar as páginas do reddit e fazer o _parsing_ delas. Um _webhook_ para o Telegram sustentado por apenas um servidor como este poderia ter problemas de escalabilidade.

Pensei então em uma solução mais complexa, porém mais escalável. Apesar de não ter familiaridade com a maioria das ferramentas utilizadas, investi nesse projeto mesmo assim. Ao fim do prazo de submissão consegui ter tanto a interface por linha de comando quanto o bot de Telegram funcionando, apesar de não ter conseguido implementar todo o planejado.
A seguir descrevo como o projeto foi planejado e quanto deste plano foi alcançado. Vou focar no desenvolvimento do bot do Telegram, pois a implementação da CLI foi basicamente um subconjunto deste.

A estrutura do projeto se baseia em microsserviços, de forma a distribuir as responsabilidades e facilitando paralelização de tarefas, além de auxiliar no escalamento horizontal de partes do sistema que estejam sobrecarregadas.

Como o desafio pede pra que sejam retornadas as _threads_ mais populares de determinados _subreddits_, pode-se imaginar que os usuários estão interessados em assuntos populares e, por isso, os _subreddits_ mais populares devem receber mais requisições. Essa alta repetibilidade de solicitações torna vantajosa a utilização de uma cache para armazenar as respostas, evitando baixar e "_parsear_ " várias vezes as mesmas páginas. Por isso um serviço de cache como o Redis poderia ser útil no projeto. [1]

Um segundo serviço a ser implementado seria um servidor REST que receberia as mensagens do Telegram, separaria os _subreddits_ e, para cada um, verificaria se a resposta já está em cache. Se não estiver, encaminha a solicitação para um _crawler_. [2]

O terceiro serviço seria um _crawler_. Diferentemente da ideia mais simples, em que o _crawler_ buscava várias páginas por requisição, aqui cada requisição só contém 1 _subreddit_ a ser pesquisado, pois a mensagem já foi quebrada anteriormente. [3]

Para diminuir o _overhead_ de comunicação entre estes serviços, foi adotada a comunicação através de [Protocol Buffers](https://developers.google.com/protocol-buffers/). Isto permite a serialização/deserialização das mensagens em formato binário de forma rápida, além de possibilitar checagem de tipos em tempo de compilação (o que não é possível com json, por exemplo). [4]

Para gerenciar a comunicação entre a fachada Rest [2] e os _crawlers_ [3], foi pensada em uma fila de mensagens. Neste caso, RabbitMQ foi escolhido. [5]

Para abstrair o envio e recebimento de mensagens através de filas, foi implementada uma camada de transporte para os serviços de RPC do Protocol Buffer [4]. Desta forma, toda a estrutura de filas, produtores e consumidores se transforma em simples chamadas RPC. [6]

Por fim, pra juntar tudo isso, Docker e Kubernetes [7].

Destes planos, a execução até o momento da entrega ficou neste ponto:

  [1] Redis: Não implementado. Apesar da importância da cache na performance do sistema, o funcionamento _end-to-end_ era prioridade.
  [2] Fachada REST: implementada.
  [3] Crawlers: implementados.
  [4] Mensageria em Protobuf: implementada.
  [5] Criação do canal RabbitMQ: implementado.
  [6] Camada de transporte para RPC usando RabbitMQ: implementada.
  [7] Docker e kubernetes: implementado.

Dito isso, é justo salientar que, dado o tamanho do projeto, a quantidade de ferramentas novas no repertório e o curto tempo para isto, a versão submetida para avaliação não contém testes e a qualidade do código ficou questionável em algumas classes. Também há algumas exceções que não foram devidamente tratadas. No fim, o projeto ficou funcional, porém instável. São falhas no projeto que estou ciente da existência e que não deixaria passar em uma rotina de trabalho.

Compilação e execução
----------------------
### CLI
Para executar a interface por linha de comando é necessário ter instalado o [Maven](https://maven.apache.org/) e o [Java Development Kit (JDK)](https://www.oracle.com/technetwork/java/javase/downloads/jdk12-downloads-5295953.html) versão 12 ou maior.

A partir desta pasta, execute o comando `./build-cli.sh` para compilar e em seguida executar o jar gerado passando como argumento uma lista de nomes de subreddits separados por ponto-e-vírgula. Ex:
```
java -jar reddit-crawler-cli/target/reddit-crawler-cli-0.0.1-SNAPSHOT-jar-with-dependencies.jar 'todayilearned;worldnews'
````

### Bot do Telegram
Para instanciar os serviços necessários para o bot é necessário ter instalado o [Maven](https://maven.apache.org/), o [Java Development Kit (JDK)](https://www.oracle.com/technetwork/java/javase/downloads/jdk12-downloads-5295953.html), o [Docker](https://www.docker.com/) e o [Kubectl](https://kubernetes.io/docs/tasks/tools/install-kubectl/). Também é necessário ter uma conta no [Docker Hub](https://hub.docker.com/) para hospedar as imagens docker geradas no processo.

Além disso é necessário criar um bot no Telegram. Para isso, abra o aplicativo e inicie uma conversa com o BotFather. Use o comando `/newbot` e siga as instruções fornecidas. Ao fim do processo o BotFather enviará uma mensagem confirmando a criação do bot contendo um **token** de identificação do bot. Este **token** não deve ser compartilhado publicamente.

Com o **token** em mãos, execute, a partir desta pasta, o comando `./configure.sh`. Será solicitado o **token** gerado pelo BotFather. Também será pedida a autenticação da ferramenta de linha de comando do Docker usando a conta do Docker Hub, caso isso não tenha sido feito anteriormente.

O script de configuração deve gerar 2 novos scripts, que devem ser executados na sequência: `./build.sh` e `./deploy.sh`. O primeiro irá compilar o projeto e criar as imagens do docker necessárias. O segundo irá fazer o _upload_ destas para o Docker Hub e subir os serviços criados no _cluster_ do Kubernetes para o qual o kubectl estiver configurado.

Um dos serviços criados no cluster será o `telegram-bot` aceitando conexões na porta 443. É necessário fornecer um certificado de segurança para este serviço, pois o bot do Telegram realiza apenas conexões HTTPS. A execução desta etapa varia dependendo de onde o _cluster_ encontra-se hospedado.

Por fim, basta configurar este servidor para funcionar como um _webhook_ para o bot do telegram. Isto pode ser feito acessando a URL
```
https://api.telegram.org/bot<TOKEN_DO_BOT>/setWebhook?url=<URL_DO_SERVIDOR>/telegram/
```
Um JSON de confirmação deve aparecer no browser.

Para executar, basta procurar pelo bot no Telegram e enviar uma mensagem /NadaParaFazer seguida de uma lista de nomes de subreddits separados por ponto-e-vírgula. Ex: `/NadaParaFazer todayilearned;worldnews`.
