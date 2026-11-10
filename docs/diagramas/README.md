# Diagramas do GyMetrics

Fonte em Mermaid (`.mmd`) de cada diagrama do documento *Modelagem do Desenvolvimento do Sistema de Software*.
Os números das seções batem com o sumário do documento. As imagens prontas para colar no documento ficam em `img/` (PNG e SVG).

Para gerar as imagens de novo depois de editar um `.mmd`: `./gerar.sh`

O GitHub desenha os blocos `mermaid` abaixo direto na página.

## 3.2.2 Diagrama de Casos de Uso (somente atores)

Fonte: [`01-atores.mmd`](01-atores.mmd) · Imagem: [`img/01-atores.png`](img/01-atores.png)

```mermaid
%% 3.2.2 Diagrama de Casos de Uso (somente atores)
flowchart BT
    REC(["👤 Recepcionista"])
    ADM(["👤 Administrador"])
    USU(["👤 Usuário do sistema"])
    REC -- generalização --> USU
    ADM -- generalização --> USU
```

## 3.2.3 / 3.2.5 Casos de Uso primários e Arquitetura em Pacotes

Fonte: [`02-casos-de-uso.mmd`](02-casos-de-uso.mmd) · Imagem: [`img/02-casos-de-uso.png`](img/02-casos-de-uso.png)

```mermaid
%% 3.2.3 / 3.2.5 Casos de Uso primários organizados em pacotes
flowchart LR
    REC(["👤 Recepcionista"])
    ADM(["👤 Administrador"])

    subgraph SEG["Pacote Segurança"]
        CSU03(["CSU03 Realizar Login"])
    end
    subgraph ALU["Pacote Alunos"]
        CSU01(["CSU01 Cadastrar Aluno"])
        CSU02(["CSU02 Buscar Aluno"])
        CSU05(["CSU05 Editar Dados do Aluno"])
        CSU07(["CSU07 Bloquear Acesso do Aluno"])
    end
    subgraph ACE["Pacote Acesso"]
        CSU08(["CSU08 Realizar Check-in"])
    end
    subgraph FIN["Pacote Financeiro"]
        CSU04(["CSU04 Registrar Pagamento"])
        CSU06(["CSU06 Cadastrar Plano"])
    end
    subgraph REL["Pacote Relatórios"]
        CSU09(["CSU09 Gerar Relatório de Frequência"])
        CSU10(["CSU10 Exportar Frequências em CSV"])
    end

    REC --- CSU03
    REC --- CSU01
    REC --- CSU02
    REC --- CSU05
    REC --- CSU07
    REC --- CSU08
    REC --- CSU04
    REC --- CSU10
    ADM --- CSU03
    ADM --- CSU06
    ADM --- CSU09

    CSU05 -. "«include»" .-> CSU02
    CSU07 -. "«include»" .-> CSU02
    CSU04 -. "«include»" .-> CSU02
```

## 3.3.1 / 4.3.1 Diagrama de Classes (domínio)

Fonte: [`03-classes-dominio.mmd`](03-classes-dominio.mmd) · Imagem: [`img/03-classes-dominio.png`](img/03-classes-dominio.png)

```mermaid
%% 3.3 / 4.3.1 Diagrama de Classes de Domínio (pacote br.com.gymetrics.negocio)
classDiagram
    direction LR
    class Aluno {
        -int id
        -String nome
        -String cpf
        -String endereco
        -String telefone
        -LocalDate dataNascimento
        -String codigoAcesso
        -Status status
        +cadastroCompleto() boolean
        +podeAcessar() boolean
        +validar() void
    }
    class Status_Aluno {
        <<enumeration>>
        ATIVO
        INADIMPLENTE
        BLOQUEADO
    }
    class Plano {
        -int id
        -String nome
        -BigDecimal valorMensal
        -int diaVencimento
        -boolean ativo
        +validar() void
    }
    class Mensalidade {
        -int id
        -LocalDate vencimento
        -BigDecimal valor
        -Status status
    }
    class Status_Mensalidade {
        <<enumeration>>
        PENDENTE
        VENCIDA
        PAGA
    }
    class Pagamento {
        -int id
        -YearMonth mesReferencia
        -BigDecimal valorPago
        -LocalDateTime dataPagamento
        +validar() void
    }
    class RegistroAcesso {
        -int id
        -LocalDateTime dataHora
        -Tipo tipo
        -String observacao
        +entrada(Aluno)$ RegistroAcesso
        +saida(Aluno)$ RegistroAcesso
    }
    class Tipo_Acesso {
        <<enumeration>>
        CHECK_IN
        CHECK_OUT
        TENTATIVA_NEGADA
    }
    class Usuario {
        -int id
        -String login
        -String senha
        -String nome
        -String perfil
        -boolean ativo
        +validar() void
    }

    Plano "0..1" <-- "0..*" Aluno : assina
    Aluno "1" <-- "0..*" Mensalidade : cobra
    Mensalidade "1" <-- "0..*" Pagamento : quita
    Aluno "0..1" <-- "0..*" RegistroAcesso : registra
    Aluno ..> Status_Aluno
    Mensalidade ..> Status_Mensalidade
    RegistroAcesso ..> Tipo_Acesso
```

## 3.3.1 Análise de Robustez — CSU08 Realizar Check-in

Fonte: [`04-robustez-checkin.mmd`](04-robustez-checkin.mmd) · Imagem: [`img/04-robustez-checkin.png`](img/04-robustez-checkin.png)

```mermaid
%% 3.3.1 Análise de Robustez - CSU08 Realizar Check-in
%% «fronteira» tela, «controle» DAO, «entidade» classe de negócio
flowchart LR
    REC(["👤 Recepcionista"])
    F["«fronteira»<br/>TelaCheckin"]
    C1(("«controle»<br/>AlunoDAO"))
    C2(("«controle»<br/>RegistroAcessoDAO"))
    E1[("«entidade»<br/>Aluno")]
    E2[("«entidade»<br/>RegistroAcesso")]

    REC --- F
    F -- "buscarPorCpfOuCodigo()" --> C1
    C1 --> E1
    F -- "registrarEntrada() / registrarSaida()" --> C2
    C2 --> E2
    E2 -. "podeAcessar()" .-> E1
```

## 4.2.1 Diagrama de Sequência — CSU03 Realizar Login

Fonte: [`05-sequencia-login.mmd`](05-sequencia-login.mmd) · Imagem: [`img/05-sequencia-login.png`](img/05-sequencia-login.png)

```mermaid
%% 4.2.1 Diagrama de Sequência - CSU03 Realizar Login
sequenceDiagram
    autonumber
    actor U as Usuário
    participant T as TelaLogin
    participant G as GymSingleton
    participant D as UsuarioDAO
    participant BD as SQLite

    U->>T: digita usuário e senha, clica "Entrar"
    T->>D: autenticar(login, senha)
    D->>BD: SELECT * FROM usuarios WHERE login=? AND senha=? AND ativo=1
    BD-->>D: linha do usuário / vazio
    alt credenciais válidas
        D-->>T: Usuario
        T->>G: setUsuarioLogado(u)
        T->>G: showTela("principal")
        G-->>U: painel conforme o perfil (RF10 / RNF13)
    else credenciais inválidas
        D->>D: soma tentativa (bloqueia na 10ª - RNF12)
        D-->>T: null
        T-->>U: "Usuário ou senha incorretos."
    end
```

## 4.2.1 Diagrama de Sequência — CSU08 Realizar Check-in

Fonte: [`06-sequencia-checkin.mmd`](06-sequencia-checkin.mmd) · Imagem: [`img/06-sequencia-checkin.png`](img/06-sequencia-checkin.png)

```mermaid
%% 4.2.1 Diagrama de Sequência - CSU08 Realizar Check-in (entrada e saída no mesmo botão)
sequenceDiagram
    autonumber
    actor R as Recepcionista
    participant T as TelaCheckin
    participant A as AlunoDAO
    participant RA as RegistroAcessoDAO
    participant E as RegistroAcesso
    participant BD as SQLite

    R->>T: digita CPF/código, clica "Confirmar"
    T->>A: buscarPorCpfOuCodigo(codigo)
    A->>BD: SELECT aluno + plano
    BD-->>A: aluno
    A-->>T: Aluno (ou null)
    T->>RA: estaPresente(aluno, hoje)
    RA->>BD: última movimentação do aluno hoje
    alt aluno já está dentro
        T->>RA: registrarSaida(aluno)
        RA->>BD: INSERT CHECK_OUT (RF06)
        T-->>R: "SAÍDA REGISTRADA"
    else aluno fora da academia
        T->>RA: registrarEntrada(aluno)
        RA->>E: entrada(aluno)
        E->>E: aluno.podeAcessar()
        E-->>RA: CHECK_IN ou TENTATIVA_NEGADA + motivo
        RA->>BD: INSERT INTO registros_acesso (RF06 / RF18)
        alt liberado
            T-->>R: "ACESSO LIBERADO" em verde (RF05 / RNF08)
        else negado
            T-->>R: motivo em vermelho: bloqueado, mensalidade vencida ou cadastro incompleto (RF03 / RF04)
        end
    end
```

## 4.2.1 Diagrama de Sequência — CSU04 Registrar Pagamento

Fonte: [`07-sequencia-pagamento.mmd`](07-sequencia-pagamento.mmd) · Imagem: [`img/07-sequencia-pagamento.png`](img/07-sequencia-pagamento.png)

```mermaid
%% 4.2.1 Diagrama de Sequência - CSU04 Registrar Pagamento
sequenceDiagram
    autonumber
    actor R as Recepcionista
    participant T as TelaGerenciamentoFinanceiro
    participant M as MensalidadeDAO
    participant P as PagamentoDAO
    participant BD as SQLite

    R->>T: abre a tela Financeiro
    T->>M: prepararMes(hoje)
    M->>BD: gera mensalidades do mês e marca vencidas
    T->>M: listarDoMes(mês atual)
    M-->>T: lista de mensalidades (RF17)
    R->>T: seleciona mensalidade, clica "Registrar Pagamento"
    T-->>R: pede mês de referência e valor pago
    R->>T: confirma
    T->>P: salvar(pagamento)
    activate P
    P->>BD: BEGIN
    P->>BD: INSERT INTO pagamentos
    P->>BD: UPDATE mensalidades SET status='PAGA'
    P->>BD: UPDATE alunos SET status='ATIVO' (se inadimplente)
    P->>BD: COMMIT
    deactivate P
    T-->>R: "Pagamento registrado" e lista atualizada
```

## 4.4.1 Diagrama de Transição de Estados — Aluno

Fonte: [`08-estados-aluno.mmd`](08-estados-aluno.mmd) · Imagem: [`img/08-estados-aluno.png`](img/08-estados-aluno.png)

```mermaid
%% 4.4.1 Diagrama de Transição de Estados - Aluno
stateDiagram-v2
    [*] --> ATIVO : cadastrar aluno (CSU01)
    ATIVO --> INADIMPLENTE : mensalidade vence sem pagamento
    INADIMPLENTE --> ATIVO : registrar pagamento (CSU04)
    ATIVO --> BLOQUEADO : bloquear acesso com motivo (CSU07)
    INADIMPLENTE --> BLOQUEADO : bloquear acesso com motivo (CSU07)
    BLOQUEADO --> ATIVO : editar situação (CSU05)
    note right of ATIVO
        Só entra na catraca se ATIVO
        e com cadastro completo
    end note
```

## 4.4.1 Diagrama de Transição de Estados — Mensalidade

Fonte: [`09-estados-mensalidade.mmd`](09-estados-mensalidade.mmd) · Imagem: [`img/09-estados-mensalidade.png`](img/09-estados-mensalidade.png)

```mermaid
%% 4.4.1 Diagrama de Transição de Estados - Mensalidade
stateDiagram-v2
    [*] --> PENDENTE : virada do mês (prepararMes)
    PENDENTE --> PAGA : registrar pagamento
    PENDENTE --> VENCIDA : data de vencimento passou
    VENCIDA --> PAGA : registrar pagamento
    PAGA --> [*]
```

## 4.5.3 Diagrama de Atividades — validação da catraca

Fonte: [`10-atividade-checkin.mmd`](10-atividade-checkin.mmd) · Imagem: [`img/10-atividade-checkin.png`](img/10-atividade-checkin.png)

```mermaid
%% 4.5.3 Diagrama de Atividades - catraca com botão único (TelaCheckin.confirmar)
flowchart TD
    A([Início]) --> B[/Recepcionista digita CPF ou código e confirma/]
    B --> C{Aluno encontrado?}
    C -- não --> N1[Exibe ALUNO NÃO ENCONTRADO]
    C -- sim --> P{Já entrou hoje<br/>e não saiu?}
    P -- sim --> S[Grava CHECK_OUT com data e hora]
    S --> S2[Exibe SAÍDA REGISTRADA]
    P -- não --> D{Status = BLOQUEADO?}
    D -- sim --> N2[Grava TENTATIVA_NEGADA<br/>Acesso bloqueado]
    D -- não --> E{Status = INADIMPLENTE?}
    E -- sim --> N3[Grava TENTATIVA_NEGADA<br/>Mensalidade vencida]
    E -- não --> F{Cadastro completo?}
    F -- não --> N4[Grava TENTATIVA_NEGADA<br/>Cadastro incompleto]
    F -- sim --> G[Grava CHECK_IN com data e hora]
    G --> H[Exibe ACESSO LIBERADO em verde]
    N2 --> X[Exibe motivo em vermelho]
    N3 --> X
    N4 --> X
    N1 --> Z([Fim])
    X --> Z
    H --> Z
    S2 --> Z
```

## 4.6.1 Diagrama de Pacotes

Fonte: [`11-pacotes.mmd`](11-pacotes.mmd) · Imagem: [`img/11-pacotes.png`](img/11-pacotes.png)

```mermaid
%% 4.6.1 Diagrama de Pacotes
flowchart TB
    subgraph APP["br.com.gymetrics"]
        MAIN["Main<br/>GymSingleton<br/>Data<br/>SetupExecCallback"]
    end
    subgraph TELAS["br.com.gymetrics.telas"]
        T["TelaLogin · TelaPrincipal · TelaCheckin<br/>TelaGerenciamentoAlunos · TelaGerenciamentoPlanos<br/>TelaGerenciamentoFinanceiro · TelaRelatorios<br/>Estilo · Atualizavel"]
    end
    subgraph DAO["br.com.gymetrics.dao"]
        D["AlunoDAO · PlanoDAO · UsuarioDAO<br/>MensalidadeDAO · PagamentoDAO · RegistroAcessoDAO"]
    end
    subgraph NEG["br.com.gymetrics.negocio"]
        N["Aluno · Plano · Mensalidade<br/>Pagamento · RegistroAcesso · Usuario"]
    end
    subgraph UTIL["br.com.gymetrics.util"]
        U["ExportadorCSV"]
    end

    MAIN --> TELAS
    MAIN --> DAO
    TELAS --> MAIN
    TELAS --> DAO
    TELAS --> NEG
    TELAS --> UTIL
    DAO --> MAIN
    DAO --> NEG
    UTIL --> NEG
```

## 4.6.2 / 4.6.3 Diagrama de Componentes e Implantação

Fonte: [`12-implantacao.mmd`](12-implantacao.mmd) · Imagem: [`img/12-implantacao.png`](img/12-implantacao.png)

```mermaid
%% 4.6.2 / 4.6.3 Diagrama de Componentes e Implantação
flowchart LR
    subgraph PC["«dispositivo» Computador da recepção"]
        subgraph JVM["«ambiente» Java 21+ (JVM)"]
            APPJ["«componente» Gymetrics<br/>(Swing)"]
            JDBC["«componente» sqlite-jdbc 3.53"]
        end
        DB[("«artefato» gymetrics.db<br/>(SQLite)")]
        CSV[/"«artefato» frequencias.csv"/]
    end
    TEC["Teclado e mouse (RNF10)"] --> APPJ
    APPJ -- "java.sql / JDBC" --> JDBC
    JDBC -- "leitura/escrita de arquivo" --> DB
    APPJ -- "exportar (RF07)" --> CSV
```

## 4.7.2 Modelo Relacional Normalizado (MRN)

Fonte: [`13-mrn.mmd`](13-mrn.mmd) · Imagem: [`img/13-mrn.png`](img/13-mrn.png)

```mermaid
%% 4.7.2 Modelo Relacional Normalizado (tabelas criadas em Data.criarTabelas)
erDiagram
    planos |o--o{ alunos : "plano_id"
    alunos ||--o{ mensalidades : "aluno_id"
    mensalidades ||--o{ pagamentos : "mensalidade_id"
    alunos |o--o{ registros_acesso : "aluno_id"
    alunos ||--o{ bloqueios_acesso : "aluno_id"
    usuarios |o--o{ bloqueios_acesso : "usuario_id"

    planos {
        INTEGER id PK
        TEXT nome "NOT NULL"
        REAL valor_mensal "NOT NULL"
        INTEGER dia_vencimento "NOT NULL"
        INTEGER ativo "DEFAULT 1"
    }
    alunos {
        INTEGER id PK
        TEXT nome "NOT NULL"
        TEXT cpf UK "NOT NULL"
        TEXT endereco
        TEXT telefone
        TEXT data_nascimento
        TEXT codigo_acesso UK
        TEXT status "ATIVO, INADIMPLENTE, BLOQUEADO"
        INTEGER plano_id FK
    }
    usuarios {
        INTEGER id PK
        TEXT login UK "NOT NULL"
        TEXT senha "NOT NULL"
        TEXT nome "NOT NULL"
        TEXT perfil "ADMINISTRADOR, RECEPCIONISTA"
        INTEGER ativo "DEFAULT 1"
    }
    mensalidades {
        INTEGER id PK
        INTEGER aluno_id FK
        TEXT vencimento "NOT NULL"
        REAL valor "NOT NULL"
        TEXT status "PENDENTE, VENCIDA, PAGA"
    }
    pagamentos {
        INTEGER id PK
        INTEGER mensalidade_id FK
        TEXT mes_referencia "NOT NULL"
        REAL valor_pago "NOT NULL"
        TEXT data_pagamento "NOT NULL"
    }
    registros_acesso {
        INTEGER id PK
        INTEGER aluno_id FK
        TEXT data_hora "NOT NULL"
        TEXT tipo "CHECK_IN, CHECK_OUT, TENTATIVA_NEGADA"
        TEXT observacao
    }
    bloqueios_acesso {
        INTEGER id PK
        INTEGER aluno_id FK
        TEXT motivo "NOT NULL"
        TEXT data_hora "NOT NULL"
        INTEGER usuario_id FK
    }
```

## 4.3.1 / 4.3.2 Diagrama de Classes completo (entidades, DAOs, utilitárias e telas)

Fonte: [`14-classes-sistema.mmd`](14-classes-sistema.mmd) · Imagem: [`img/14-classes-sistema.png`](img/14-classes-sistema.png)

```mermaid
%% 4.3.1 / 4.3.2 Diagrama de Classes completo (entidades, controle/DAO, utilitárias e fronteira)
%% Getters e setters das entidades foram omitidos para legibilidade.
classDiagram
    direction TB

    namespace gymetrics {
        class Main {
            +main(String[] args)$
        }
        class GymSingleton {
            <<singleton>>
            -GymSingleton instance$
            -CardLayout telasLayout
            -Map~String,Component~ porNome
            -Data data
            -Usuario usuarioLogado
            +getInstance()$ GymSingleton
            +showTela(String nome)
            +getUsuarioLogado() Usuario
            +setUsuarioLogado(Usuario u)
            +isAdmin() boolean
            +sair()
            +alunos() AlunoDAO
            +planos() PlanoDAO
            +usuarios() UsuarioDAO
            +pagamentos() PagamentoDAO
            +mensalidades() MensalidadeDAO
            +acessos() RegistroAcessoDAO
            +showMsg(String texto)
            +showErro(Exception e)
            +executar(Acao acao) boolean
            +takeFrame() JPanel
            -criarUsuariosIniciais()
        }
        class Data {
            -Connection mConnection
            +open(String arquivo)
            +exec(String sql, SetupExecCallback setup, RowMapper~T~ mapper) List~T~
            +insert(String sql, SetupExecCallback setup) int
            +update(String sql, SetupExecCallback setup) int
            +run(String sql)
            +transacao(SqlAction acao)
            +criarTabelas()
            +suicide()
        }
        class SetupExecCallback {
            <<interface>>
            +setupExecParams(PreparedStatement ps)
        }
    }

    namespace negocio {
        class Aluno {
            -int id
            -String nome
            -String cpf
            -String endereco
            -String telefone
            -LocalDate dataNascimento
            -String codigoAcesso
            -Status status
            -Plano plano
            +cadastroCompleto() boolean
            +podeAcessar() boolean
            +validar()
        }
        class Plano {
            -int id
            -String nome
            -BigDecimal valorMensal
            -int diaVencimento
            -boolean ativo
            +validar()
        }
        class Mensalidade {
            -int id
            -Aluno aluno
            -LocalDate vencimento
            -BigDecimal valor
            -Status status
        }
        class Pagamento {
            -int id
            -Mensalidade mensalidade
            -YearMonth mesReferencia
            -BigDecimal valorPago
            -LocalDateTime dataPagamento
            +validar()
        }
        class RegistroAcesso {
            -int id
            -Aluno aluno
            -LocalDateTime dataHora
            -Tipo tipo
            -String observacao
            +entrada(Aluno a)$ RegistroAcesso
            +saida(Aluno a)$ RegistroAcesso
        }
        class Usuario {
            -int id
            -String login
            -String senha
            -String nome
            -String perfil
            -boolean ativo
            +validar()
        }
    }

    namespace dao {
        class AlunoDAO {
            -Data db
            +salvar(Aluno a)
            +atualizar(Aluno a)
            +buscar(String termo) List~Aluno~
            +buscarPorCpfOuCodigo(String valor) Aluno
            +contar(Status status) int
            +bloquear(Aluno a, String motivo, Usuario responsavel)
        }
        class PlanoDAO {
            -Data db
            +salvar(Plano p)
            +atualizar(Plano p)
            +listar() List~Plano~
        }
        class MensalidadeDAO {
            -Data db
            +prepararMes(LocalDate hoje)
            +listarDoMes(YearMonth mes) List~Mensalidade~
            +vencendo(LocalDate hoje, int dias) List~Mensalidade~
        }
        class PagamentoDAO {
            -Data db
            +salvar(Pagamento p)
        }
        class RegistroAcessoDAO {
            -Data db
            +salvar(RegistroAcesso r)
            +registrarEntrada(Aluno a) RegistroAcesso
            +registrarSaida(Aluno a) RegistroAcesso
            +estaPresente(Aluno a, LocalDate dia) boolean
            +contarEntradas(LocalDate dia) int
            +contarPresentes(LocalDate dia) int
            +listarPorPeriodo(LocalDate inicio, LocalDate fim) List~RegistroAcesso~
        }
        class UsuarioDAO {
            -int MAX_TENTATIVAS$
            -Data db
            -Map~String,Integer~ tentativas
            +salvar(Usuario u)
            +contar() int
            +estaBloqueado(String login) boolean
            +autenticar(String login, String senha) Usuario
        }
    }

    namespace util {
        class ExportadorCSV {
            <<utility>>
            +exportarAcessos(List~RegistroAcesso~ lista, Path arquivo)$
        }
    }

    namespace telas {
        class Atualizavel {
            <<interface>>
            +atualizar()
        }
        class Estilo {
            <<utility>>
            +pagina()$ JPanel
            +cabecalho(String titulo, String subtitulo)$ JPanel
            +botao(String texto, Runnable acao)$ Botao
            +tabela(String... colunas)$ JTable
            +formulario(...)$ boolean
        }
        class TelaLogin {
            -entrar()
        }
        class TelaPrincipal
        class TelaCheckin {
            -confirmar()
        }
        class TelaGerenciamentoAlunos {
            -cadastrar()
            -editar()
            -bloquear()
        }
        class TelaGerenciamentoPlanos {
            -cadastrar()
            -editar()
            -alternar()
        }
        class TelaGerenciamentoFinanceiro {
            -registrarPagamento()
        }
        class TelaRelatorios {
            -gerar()
            -exportar()
        }
        class TelaDebug
    }

    %% associações entre entidades
    Aluno "0..*" --> "0..1" Plano : assina
    Mensalidade "0..*" --> "1" Aluno : cobra
    Pagamento "0..*" --> "1" Mensalidade : quita
    RegistroAcesso "0..*" --> "0..1" Aluno : registra

    %% infraestrutura
    Main ..> GymSingleton : getInstance()
    GymSingleton *-- Data
    GymSingleton *-- AlunoDAO
    GymSingleton *-- PlanoDAO
    GymSingleton *-- MensalidadeDAO
    GymSingleton *-- PagamentoDAO
    GymSingleton *-- RegistroAcessoDAO
    GymSingleton *-- UsuarioDAO
    GymSingleton --> Usuario : usuarioLogado
    GymSingleton o-- Atualizavel : telas
    Data ..> SetupExecCallback

    %% DAOs persistem as entidades (o uso de Data aparece no atributo -Data db)
    AlunoDAO ..> Aluno
    PlanoDAO ..> Plano
    MensalidadeDAO ..> Mensalidade
    PagamentoDAO ..> Pagamento
    RegistroAcessoDAO ..> RegistroAcesso
    UsuarioDAO ..> Usuario
    ExportadorCSV ..> RegistroAcesso

    %% telas
    Atualizavel <|.. TelaLogin
    Atualizavel <|.. TelaPrincipal
    Atualizavel <|.. TelaCheckin
    Atualizavel <|.. TelaGerenciamentoAlunos
    Atualizavel <|.. TelaGerenciamentoPlanos
    Atualizavel <|.. TelaGerenciamentoFinanceiro
    Atualizavel <|.. TelaRelatorios
    TelaRelatorios ..> ExportadorCSV
```
