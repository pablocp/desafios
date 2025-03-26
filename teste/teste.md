# Proposta de modelo para envio de dados ao Bacen

Este documento visa propor um modelo de XML para atender à solicitação do Banco Central do Brasil a respeito do envio de dados de indícios de ocorrências e tentativas de fraudes registrados pelas casas participantes do consórcio de interoperabilidade da Resolução Conjunta n.6/2023.

## Exemplo de XML proposto
```xml
<documento_fraudes tipo="I" cnpj="77422075000195" data_base="202504">
    <ocorrencias>
        <ocorrencia>
            <registro data_hora="2024-08-24T14:15:22Z" local="R. Major Brigadeiro Capitão, 123, cep 12345678 " canal="2" atividade_relacionada="1" classificacao="1" motivo="Motivo do relato" valor_transacao="2.65" valor_contrato="0" envolvimento_reclamante="1">
                <dispositivo identificacao="computador pessoal" ip="192.168.0.1"/>
            </registro>
            <entidade_registradora cnpj_origem="63513297000109" data_registro="2024-08-25T17:31:22Z"/>
            <informacoes_bancarias_destino codigo_instituicao="63513297000109" agencia="5100">
                <conta numero="101010" tipo="1">
                    <titular>
                        <documento tipo="1" numero="114557633354"/>
                    </titular>
                    <documentos_representante_legal>
                        <documento tipo="1" numero="114557000000"/>
                        <documento tipo="1" numero="114557633354"/>
                    </documentos_representante_legal>
                </conta>
            </informacoes_bancarias_destino>
            <informacao_executor>
                <documento tipo="1" numero="11122233347"/>
                <documentos_representante_legal>
                    <documento tipo="1" numero="114557000000"/>
                    <documento tipo="1" numero="114557633354"/>
                </documentos_representante_legal>
            </informacao_executor>
            <informacao_reclamante>
                <documento tipo="1" numero="11122233347"/>
                <documentos_representante_legal>
                    <documento tipo="1" numero="114557000000"/>
                    <documento tipo="1" numero="114557633354"/>
                </documentos_representante_legal>
            </informacao_reclamante>
        </ocorrencia>
    </ocorrencias>
</documento_fraudes>
```

## Descrição das Tags

### Tag `documento_fraudes`
- **Descrição**: Tag raíz do documento. Inclui informações sobre todos os registros de fraudes ativos.
- **Atributos**:
    - **tipo**: Preencher com "I" (vogal i maiúscula), indicando que o arquivo se refere ao cadastro de novos dados, ou "S", indicando que o arquivo se refere à substituição (correção) de dados cadastrados anteriormente.
    - **cnpj**: Preencher com o CNPJ da casa responsável pelos registros (apenas dígitos)
    - **data_base**: Preencher com o ano e mês, no formato "yyyyMM", indicando que os dados que constam no arquivo são referentes aos registros até o mês indicado.
- **Sub-tags**:
    - [ocorrencias](#tag-ocorrencias): cardinalidade 1

### Tag `ocorrencias`
- **Descrição**: Tag para agrupar todos os registros de fraudes ativos.
- **Atributos**: _sem atributos_
- **Sub-tags**:
    - [ocorrencia](#tag-ocorrencia): cardinalidade 1 ou mais

### Tag `ocorrencia`
- **Descrição**: Apresenta os dados relativos a um único registro de fraude.
- **Atributos**: _sem atributos_
- **Sub-tags**:
    - [registro](#tag-registro): cardinalidade 1
    - [entidade_registradora](#tag-entidade_registradora): cardinalidade 1
    - [informacoes_bancarias_destino](#tag-informacoes_bancarias_destino): cardinalidade 1
    - [informacao_executor](#tag-informacao_executor): cardinalidade 1
    - [informacao_reclamante](#tag-informacao_reclamante): cardinalidade 1

### Tag `registro`
- **Descrição**: Apresenta os dados básicos sobre o registro da fraude
- **Atributos**:
    - **data_hora**: data e hora da ocorrência ou tentativa da fraude, no formato ISO 8601
    - **local**: endereço da ocorrência ou tentativa de fraude
    - **canal**: canal que teria sido utilizado para a fraude ou tentativa de fraude, seguindo a [tabela de domínios de canal](#dominio-canal)
    - **atividade_relacionada**: atividade relacionada à fraude, seguindo a [tabela de domínios de atividades relacionadas](#dominio-atividades-relacionadas)
    - **classificacao**: classificação quanto a fraude tentada ou consumada, seguindo a [tabela de domínios de classificacao](#dominio-classificacao)
    - **motivo**: motivo do relato
    - **valor_transacao**:
    - **valor_contrato**:
    - **envolvimento_reclamante**:
- **Sub-tags**:
    - [dispositivo](#tag-dispositivo):
> [!Warning]
> `valor_transacao` 

### Tag `dispositivo`
- **Descrição**: Apresenta os dados do dispositivo utilizado na execução ou tentativa de fraude
- **Atributos**:
    - **identificacao**: 
    - **ip**:
- **Sub-tags**: _não há_

### Tag `entidade_registradora`
- **Descrição**: Apresenta os dados da Instituição Financeira responsável pelo cadastro da fraude e o horário em que o cadastro foi realizado
- **Atributos**:
    - **cnpj_origem**:
    - **data_hora_registro**: 
- **Sub-tags**: _não há_

### Tag `informacoes_bancarias_destino`
- **Descrição**: Apresenta os dados bancários da conta destino da transação
- **Atributos**:
    - **codigo_instituicao**: 
    - **agencia**: 
- **Sub-tags**:
    - [conta](#tag-conta): cardinalidade 1

### Tag `conta`
- **Descrição**: 
- **Atributos**:
- **Sub-tags**:
    - [titular](#tag-titular): cardinalidade 1
    - [documentos_representante_legal](#tag-documentos_representante_legal): cardinalidade 0 ou 1

### Tag `titular`
- **Descrição**: 
- **Atributos**:
- **Sub-tags**:
    - [documento](#tag-documento): cardinalidade 1

### Tag `documento`
- **Descrição**: 
- **Atributos**:
- **Sub-tags**: _não há_

### Tag `documentos_representante_legal`
- **Descrição**: 
- **Atributos**:
- **Sub-tags**:
    - [documento](#tag-documento): cardinalidade 1 ou mais

### Tag `informacao_executor`
- **Descrição**: 
- **Atributos**:
- **Sub-tags**:
    - [documento](#tag-documento): cardinalidade 1
    - [documentos_representante_legal](#tag-documentos_representante_legal): cardinalidade 0 ou 1

### Tag `informacao_reclamante`
- **Descrição**: 
- **Atributos**:
- **Sub-tags**:
    - [documento](#tag-documento): cardinalidade 1
    - [documentos_representante_legal](#tag-documentos_representante_legal): cardinalidade 0 ou 1

## Tabelas de domínios
