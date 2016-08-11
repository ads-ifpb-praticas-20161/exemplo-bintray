# Exemplo de integração com Bintray

As intruções abaixo servirão para integrar o build do Travis CI no projeto da disciplina com o Bintray, para distribuir os pacotes **RELEASE** através da internet.

## Considerações iniciais

Uma vez que estamos preparando uma RELEASE, ou seja, um artefato estável, testado e pronto para ser usado, os arquivos que forem configurados aqui ficarão na branch **MASTER**.

Versões RELEASE não podem ter qualificador **SNAPSHOT**. Portanto, antes de começar, feche uma versão do seu projeto (alterar a versão pra 1.0).

## Criar conta no Bintray

Crie uma conta no **Bintray** (pode ser uma por grupo) e informe via email do grupo o nome da conta para que possa ser incluído na *organization*. Dentro dessa organização, ficarão os pacotes de cada um dos grupos da disciplina.

> Professor, o que é o Bintray?

Bintray é um serviço para publicar, baixar e distribuir pacotes de software aberto. Nesta integração, ele vai agir de forma semelhante ao [Artifactory](https://www.jfrog.com/artifactory/), recebendo os pacotes gerados a partir dos projetos Maven e gerenciando as versões dos mesmos.

## Importar o repositório

Ao receber o convite e logar pela primeira vez no Bintray, você irá encontrar na página da organization uma seção chamada *Owned Repositories*, onde ele dispõe de repositórios para os mais variados tipos de artefatos. Selecione o repositório [Maven](https://bintray.com/ads-ifpb-praticas/maven).

Ao ser apresentado a página da organization, existe uma opção chamada **Import from Github**. Ao acionar essa função, você será apresentado a lista de repositórios que estão hospedados dentro de nossa [organization do Github](https://github.com/ads-ifpb-praticas-20161). Escolha o repositório da sua equipe.

## Configurando o Maven

### Comunicação com o servidor do Bintray

Após a importação, será mostrado um novo pacote dentro do repositório de artefatos Maven. Selecione o do seu projeto e em seguida, clique em *Set me up*. Clique em "Uploading" > "Deploying with Maven" e siga as instruções (Passo 1) para configurar o bintray como servidor em um arquivo `settings.xml`. Perceba que a senha está apenas com *** e você deve trocar por sua API Key do Bintray.

> Professor, esse settings.xml deve ser colocado em ~/.m2 igual ao do Artifactory?

**Não**. Esse arquivo deve estar dentro do seu projeto. Uma vez que, durante o build do Travis CI não temos acesso a configuração da instalação do Maven, iremos informar a configuração no momento do build.

O passo 2 será configurado nas próximas instruções, portanto, mantenha essa página aberta.

### Configurar um novo profile

Vamos configurar um novo profile chamado "RELEASE" dentro do projeto para indicar as configurações que serão utilizadas no momento de lançar uma nova versão. Sendo assim, teremos a configuração de `distributionManagement` apenas no profile de RELEASE

```xml

<profile>
    <id>release</id>
    <distributionManagement>
        <repository>
            <id>bintray-ads-ifpb-praticas-maven</id>
            <name>ads-ifpb-praticas-maven</name>
            <url>https://api.bintray.com/maven/ads-ifpb-praticas/maven/[NOME_DO_PROJETO]/;publish=1</url>
        </repository>
    </distributionManagement>
</profile>

```

Isso vai servir para que não possamos fazer release utilizando outros profiles, uma vez que eles indicam que estamos em ambientes de desenvolvimento e testes.

## Configurar o .travis.yml

Nesse momento, vamos configurar o Travis CI para realizar o build apenas de **TAGS**. Uma vez que as tags indicam que temos um marco do nosso projeto, é interessante que este build de releases seja feito apenas em cima de revisões com tag.

No seu `travis.yml`, configure a seção branches da seguinte maneira:

```yaml

branches:
  only:
  - /^v.*$/

```

Isso vai indicar que o Travis CI deve construir todos os objetos que começarem com `v`. Uma vez que estamos padronizando nossas tags para sempre terem esse prefixo, estamos reduzindo o escopo desse build. Sendo assim, a partir de agora, caso precisem alguma tag temporária, coloquem um prefixo diferente.

> Professor, ao lançar uma nova versão na branch master, eu não preciso fazer merge?

Sim, a branch **master** vai receber um merge de outra branch. Provavelmente, da branch de homologação, uma vez que esta já está teoricamente "estável". O que vai ser adicionado aqui é o tag.

> Ok, mas o merge não iria criar um conflito do travis.yml? 

Sim, mas podemos evitar isso. Pra isso que serve [gitattributes](https://git-scm.com/book/en/v2/Customizing-Git-Git-Attributes).

### Scripts de build

Agora, precisamos informar quais comandos do Maven o Travis CI vai precisar utilizar para que o build seja considerado como "sucesso". O seguinte script será utilizado

    script:
      mvn deploy -Prelease --settings settings.xml

Significa fazer deploy para os repositórios definidos em `distributionManagement`, que estão definidos no profile RELEASE (`-Prelease`), utilizando como configuração o arquivo que criamos (`--settings settings.xml`). Dessa maneira configuramos a nossa conta do Bintray no repositório em tempo de build.

## Considerações finais

Faça push para a branch master no repositório remoto e espere o Build do Travis. Ao final, volte ao organization no Bintray e clique no seu pacote.  Você deve ver uma nova versão criada.

### Links úteis

https://github.com/bintray/bintray-examples/tree/master/travis-ci-example