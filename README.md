# 🐉 Gerenciador e Balanceador de Bestiário D&D 5e (Trabalho de POO)

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Swing](https://img.shields.io/badge/Java_Swing-GUI-007396?style=for-the-badge&logo=java&logoColor=white)
![POO](https://img.shields.io/badge/Paradigma-POO-success?style=for-the-badge)

Este projeto foi desenvolvido como trabalho prático para a disciplina de **Programação Orientada a Objetos (POO)**. Trata-se de uma aplicação desktop em Java (com interface gráfica em Swing) para criação, gerenciamento, persistência e **balanceamento matemático automático de fichas de monstros** de *Dungeons & Dragons (D&D 5e)*.

---

## 🎯 Funcionalidades Principais

* **Cadastro Completo de Criaturas:** Criação interativa de monstros definindo atributos básicos (STR, DEX, CON, INT, WIS, CHA), Classe de Armadura (CA), Vida (HP), deslocamento, sentidos, idiomas e Nível de Desafio (*Challenge Rating* - CR).
* **Sistema de Ações e Magias:** Adição ilimitada de ataques corpo a corpo (*Melee*), ataques à distância (*Ranged*), habilidades passivas, habilidades ativas (Ações, Ações Bônus, Ações Lendárias com recarga) e conjuração de magias (*Spellcasting* com cálculo automático de *Save DC*).
* **Analisador e Balanceador de CR (`BalanceadorCRService`):** Compara o CR imaginado pelo usuário com o **CR Real** da criatura, calculado a partir das tabelas oficiais de D&D (cruzando HP, CA, Bônus de Ataque, Dano Médio por rodada e CD de Magia)[cite: 1]. Caso haja desbalanceamento, o sistema oferece 3 opções[cite: 1]:
  1. Manter a ficha como está[cite: 1].
  2. Atualizar o título do CR da criatura para o valor real calculado[cite: 1].
  3. **Rebalancear a ficha automaticamente**, ajustando HP, CA, bônus de ataque, dano e CD de magias para se adequarem ao CR desejado[cite: 1].
* **Busca e Remoção:** Pesquisa de monstros pelo nome (ignorando maiúsculas/minúsculas) com opção de exclusão do bestiário[cite: 1].
* **Persistência em Arquivo Binário:** Salvamento e carregamento de todo o bestiário no arquivo `bestiario.dat` utilizando serialização nativa do Java[cite: 1].

---

## 🧠 Conceitos de POO Aplicados no Projeto

O projeto foi arquitetado para aplicar na prática todos os pilares da Orientação a Objetos[cite: 1]:

### 1. Abstração e Herança
* **`EntidadeRPG` (Classe Abstrata):** Representa a base de qualquer criatura do sistema, encapsulando atributos comuns e definindo métodos abstratos obrigatórios como `formatarFicha()`, `aplicarCaracteristicasDoTipo()` e `calcularHpFinal()`[cite: 1].
* **Subclasses Especializadas (`MonstroPadrao`, `MortoVivo`, `Construto`):** Herdam de `EntidadeRPG` e implementam regras biológicas próprias automaticamente no construtor[cite: 1]:
  * **`MortoVivo`:** Recebe automaticamente imunidades a veneno e exaustão, a habilidade passiva *Undead Nature* e ajuste de -5% no cálculo de HP final[cite: 1].
  * **`Construto`:** Recebe automaticamente imunidades a condições mentais/físicas (*Charmed*, *Frightened*, *Paralyzed*, *Petrified*), a passiva *Construct Nature* e ajuste de -5% no HP final[cite: 1].
  * **`MonstroPadrao`:** Criatura genérica cujo HP final equivale a 100% do seu HP base[cite: 1].

### 2. Polimorfismo (Sobrescrita e Coleções Polimórficas)
* **Coleção Principal:** O serviço gerencia uma `List<EntidadeRPG>`, tratando diferentes subtipos de criaturas de maneira uniforme[cite: 1]. Na renderização da tela (`atualizarTela()`), a chamada polimórfica `m.formatarFicha()` executa o comportamento específico de cada classe filha[cite: 1].
* **Ações de Combate:** Cada criatura possui uma lista polimórfica `List<AcaoCombate>`, capaz de armazenar ataques físicos, magias e habilidades passivas/ativas na mesma estrutura[cite: 1].

### 3. Composição e Interfaces
* **`AcaoCombate` (Interface):** Implementada por `AtaqueCorpoACorpo`, `AtaqueRanged`, `HabilidadeAtiva`, `HabilidadePassiva` e `Magia`, compondo as capacidades de uma `EntidadeRPG`[cite: 1].
* **`MultiAtaque` (Interface):** Implementada por `AtaqueCorpoACorpo` e `AtaqueRanged` para ataques múltiplos na mesma rodada[cite: 1].
* **`CalculadorEstatistica` (Interface):** Assinada por `EntidadeRPG` para padronizar o método `calcularBonusProficiencia()` com base na faixa de CR (de +2 a +8)[cite: 1].

### 4. Tratamento de Exceções (Checked vs. Unchecked)
* **Exceção Checked (`MonstroNaoEncontradoException`):** Estende `Exception`[cite: 1]. Obriga o tratamento explícito com `try/catch` na camada de visualização quando uma busca ou remoção não encontra o registro[cite: 1].
* **Exceção Unchecked (`RegraMatematicaInvalidaException`):** Estende `RuntimeException`[cite: 1]. Lançada quando uma regra de domínio do D&D é violada (ex.: tentar cadastrar um monstro com `CR < 0` ou `CR > 30`)[cite: 1].

### 5. Serialização e I/O
* Uso da interface `Serializable` em `EntidadeRPG` e `AcaoCombate`, permitindo que toda a árvore de objetos (monstros e suas listas internas de ações e mapas de magias) seja gravada e recuperada em disco via `ObjectOutputStream` e `ObjectInputStream` com fechamento automático de recursos (*try-with-resources*)[cite: 1].

---

## 📂 Arquitetura de Pacotes

O código-fonte principal (`src/br/com/dnd/`) está organizado em camadas para separar responsabilidades[cite: 1]:

```text
src/br/com/dnd/
├── exception/
│   ├── MonstroNaoEncontradoException.java      # Exceção Checked (busca/remoção)
│   └── RegraMatematicaInvalidaException.java   # Exceção Unchecked (validação de CR 0-30)
├── model/
│   ├── EntidadeRPG.java                        # Classe abstrata mãe
│   ├── MonstroPadrao.java                      # Subclasse de EntidadeRPG
│   ├── MortoVivo.java                          # Subclasse de EntidadeRPG
│   ├── Construto.java                          # Subclasse de EntidadeRPG
│   ├── CalculadorEstatistica.java              # Interface de cálculo de proficiência
│   ├── AcaoCombate.java                        # Interface base para ações (Serializable)
│   ├── MultiAtaque.java                        # Interface para múltiplos ataques
│   ├── AtaqueCorpoACorpo.java                  # Ação de ataque Melee
│   ├── AtaqueRanged.java                       # Ação de ataque à distância
│   ├── HabilidadeAtiva.java                    # Ações, Ações Bônus e Lendárias
│   ├── HabilidadePassiva.java                  # Traços e habilidades passivas
│   └── Magia.java                              # Estrutura de Spellcasting (LinkedHashMap)
├── persistence/
│   └── ArquivoBestiarioDAO.java                # Leitura e escrita binária (bestiario.dat)
├── service/
│   ├── GerenciadorBestiarioService.java        # Regras de negócio e CRUD em memória
│   └── BalanceadorCRService.java               # Tabelas oficiais e algoritmo de CR
└── view/
    └── TelaPrincipal.java                      # Interface gráfica em Java Swing (JFrame)
