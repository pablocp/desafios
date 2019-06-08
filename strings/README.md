Desafio 1: Strings
==================
Neste desafio foi pedido um programa que limitasse a quantidade de caracteres por linha em um texto dado como entrada, inserindo quebras de linha quando necessário, porém respeitando os limites das palavras. Como continuação do problema, também foi pedido que houvesse a possibilidade de justificar o texto e de parametrizar a quantidade máxima de caracteres por linha.

Solução
-------
A primeira ideia para resolver o problema foi primeiramente dividir o texto em várias `Strings` de acordo com as quebras de linha originais. Desta forma seria fácil devolvê-las ao texto após o processamento.

Em seguida, para cada linha do texto original, iterar sobre suas palavras até que o limite de caracteres fosse atingido, inserindo, então, uma quebra de linha e continuando a iteração na linha seguinte. Esta solução se adapta facilmente ao requisito de possibilitar a parametrização do tamanho das linhas, visto que não depende de um número fixo de caracteres.

Pensei, então em duas abordagens para a implementação desta solução: da forma tradicional, utilizando laços como `for` e `while` para percorrer a entrada, ou transformando o texto em uma _stream_ de dados e utilizando as operações sobre `Stream`disponíveis em Java.

Uma pesquisa rápida de _benchmarks_ mostrou que a primeira alternativa apresenta melhores resultados em termos de desempenho, apresentando desempenho significativamente inferior apenas para grandes volumes de dados quando comparada a _streams_ de processamento em paralelo, porém ainda com a ressalva de que a tarefa em questão deve ser intrinsicamente paralelizável para que isto ocorra.

Sendo assim, a opção que se mostrou mais adequada para a resolução deste problema se faz utilizando os blocos de repetição da linguagem. Entretanto esta não foi a solução implementada por mim, e explico o porquê:

Se este problema tivesse surgido em uma situação cotidiana, certamente adotaria a implementação mais simples e mais eficiente. Porém este problema surgiu no contexto de um processo seletivo de uma vaga de desenvolvimento Java. Sendo assim, entendi que teria pouco a mostrar com a solução mais básica, tendo preferido utilizar _streams_ para demonstrar familiaridade com construções mais novas da linguagem, além de facilidade em raciocinar sobre este modelo de processamento que se mostra bastante relevante no cenário atual, em que a demanda por processamento de grandes volumes de dados se faz cada vez mais presente.

Por fim, para resolver o requisito de justificar o texto, foi adotada uma política de 3 passagens sobre o texto: a primeira visava apenas dividir as palavras em linhas de acordo com o limite de caracteres estabelecido. A segunda para calcular qual das linhas geradas era a maior (isto poderia ter sido feito na primeira passagem, mas só pensei nisso depois de ter terminado a implementação e achei que o ganho de performance não valeria o esforço de mudar o código nesse caso específico). A terceira passagem foi para ajustar o espaço entre as palavras de cada linha de forma que todas as linhas de forma que elas ficassem com o tamanho da maior linha. Apesar de ser possível fazer tudo isso em uma passagem só, utilizando o limite de caracteres como objetivo do tamanho da linha, isto produziria um resultado mais feio em alguns casos. Por outro lado, esta solução com uma única passagem poderia ser facilmente aplicada a grandes quantidades de texto, visto que não exige que os resultados intermediários fiquem armazenados em memória.

Compilação e execução
---------------------
Para compilar este projeto será necessária a instalação do [Bazel](https://bazel.build).

A partir desta pasta, use o comando:

```
bazel build //src/main/java:Main
```

Após a compilação, execute o programa usando:

```
./bazel-bin/src/main/java/Main
```

Os testes unitários podem ser compilados e executados através dos comandos:

```
bazel build //src/test/java:LimitedTextLineTest
```
```
./bazel-bin/src/test/java/LimitedTextLineTest
```

e

```
bazel build //src/test/java:LimitedTextLineTest
```
```
./bazel-bin/src/test/java/LimitedTextLineTest
```
