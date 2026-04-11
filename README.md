# AppHub 📱

Aplicativo hub em Kotlin desenvolvido como trabalho final da Unidade I da disciplina de Desenvolvimento Mobile (DIM0524).

## Sobre o projeto

O AppHub centraliza três aplicativos em um único ponto de acesso, permitindo navegar entre eles a partir de uma tela principal.

## Aplicativos integrados

| App | Descrição |
|-----|-----------|
| 🏀 Placar de Basquete | Controle de pontuação para partidas de basquete |
| 🧮 Calculadora Científica | Calculadora com operações científicas |
| 📝 Lista de Tarefas | Gerenciamento simples de tarefas do dia a dia |

## Estrutura do projeto

```
AppHub/
├── MainActivity.kt          # Tela principal (Hub)
├── BasqueteActivity.kt      # Placar de basquete
├── CalculadoraActivity.kt   # Calculadora científica
├── TarefasActivity.kt       # Lista de tarefas
└── res/
    ├── values/
    │   ├── strings.xml      # Textos centralizados
    │   ├── colors.xml       # Cores centralizados
    │   └── dimens.xml       # Dimensões centralizadas
    └── layout/              # Layouts XML de cada tela
```

## Tecnologias utilizadas

- **Linguagem:** Kotlin
- **UI:** Jetpack Compose
- **Navegação:** Intent entre Activities
- **Mínimo SDK:** API 24 (Android 7.0)

## Requisitos

- Android Studio Hedgehog ou superior
- JDK 17+
- Dispositivo ou emulador com Android 7.0+

## Como executar

1. Clone o repositório:
```bash
git clone https://github.com/seu-usuario/apphub.git
```

2. Abra o projeto no Android Studio

3. Aguarde a sincronização do Gradle

4. Execute em um emulador ou dispositivo físico

## Funcionalidades

- Tela principal com menu de navegação entre os apps
- Navegação via `Intent` com retorno ao hub
- Tema claro e escuro
- Layout responsivo adaptado para diferentes tamanhos de tela
- Cores, textos e dimensões centralizados em arquivos de recursos
- Contraste de cores seguindo as diretrizes WCAG (4.5:1 para texto normal)

## Autor

Desenvolvido como trabalho acadêmico — UFRN / DIM0524