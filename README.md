## Telegram Bot Starter (Spring Boot)

**Telegram Bot Starter** is a small Spring Boot library that helps you build Telegram bots with a **stateful, menu/step–based flow**.  
It wraps `telegrambots-spring-boot-starter` and provides:

- **Auto‑configuration** via Spring Boot starter semantics
- **Long‑polling or webhook** bot modes (switchable by property)
- **User context storage** with a pluggable `UserContextStorage` (in‑memory implementation by default)
- A **declarative DSL** (`MenuSectionBuilder`, `BotStepBuilder`) for building multi‑step conversational flows
- A simple **API service** (`BotApiService`) to send and clear messages

This project is published as a **Maven/Gradle dependency** for other developers to quickly create Telegram bots with conversational context.

---

### 1. Getting started

#### 1.1. Dependency

Add the GitHub Packages repository and the library dependency.

**Gradle (Groovy)**:

```groovy
repositories {
    mavenCentral()
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/BuzAleks/telegram-bot-starter")
        credentials {
            username = findProperty("gpr.user") ?: System.getenv("GITHUB_USERNAME")
            password = findProperty("gpr.token") ?: System.getenv("GITHUB_TOKEN")
        }
    }
}

dependencies {
    implementation "link.buzalex:telegram-bot-starter:<version>"
}
```

**Maven**:

```xml
<repositories>
    <repository>
        <id>github-buzalex-telegram-bot-starter</id>
        <url>https://maven.pkg.github.com/BuzAleks/telegram-bot-starter</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>link.buzalex</groupId>
        <artifactId>telegram-bot-starter</artifactId>
        <version><!-- put latest version here --></version>
    </dependency>
</dependencies>
```

---

### 2. Auto‑configuration & basic setup

The library is a Spring Boot auto‑configuration module:

- `TelegramBotAutoConfiguration` is registered under `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`
- It enables `TelegramBotProperties` and performs a `@ComponentScan("link.buzalex")`

Once on the classpath and with Spring Boot auto‑configuration enabled, it will:

- Create **long‑polling bot** (`TelegramLongPollingHandler`) by default, or
- Create **webhook bot** (`TelegramWebhookHandler` + `BotWebhookRestController`) when configured
- Wire up the internal **menu manager**, **context storage**, **message converter**, **exception handler**, and **BotApiService**

#### 2.1. Required configuration

Add the following properties to `application.yml` (or `application.properties`):

```yaml
telegram:
  bot:
    username: YOUR_BOT_USERNAME
    token: YOUR_BOT_TOKEN

    # "longPolling" (default) or "webhook"
    type: longPolling

    webhook:
      # Used only when type = webhook
      url: https://your-domain.com       # public base URL
      path: /telegram/webhook            # mapping path and Telegram webhook path
```

**Notes:**

- `telegram.bot.username` and `telegram.bot.token` are required for both modes.
- `telegram.bot.type` (optional) – defaults to `longPolling`.
- When `type = webhook`:
  - `TelegramWebhookHandler` is created.
  - On startup, the library calls `SetWebhook` with `webhook.url + webhook.path`.
  - A `POST` endpoint is exposed at `telegram.bot.webhook.path` via `BotWebhookRestController`.

---

### 3. Conversation model: Menus, steps and context

The core of this starter is a **menu/step DSL** plus a **user context** that persists data between messages.

- A **menu section** is represented by `MenuSection` and built via `MenuSectionBuilder`.
- Each menu consists of multiple **steps**, built via `BotStepBuilder`.
- Each incoming update is mapped to a `BotMessage`, and the **menu manager** decides which section/step to run based on:
  - The current **user context**
  - Section **selectors** (predicates on `BotMessage`)

#### 3.1. User context

The default context implementation is `UserContextImpl`, and contexts are stored using:

- `UserContextStorage<UserContext>` abstraction
- Default in‑memory implementation: `InMemoryUserContextStorageImpl`

You can override the storage by providing your own `UserContextStorage` bean. Due to Spring’s `@ConditionalOnMissingBean`, the in‑memory implementation is used only when no custom bean is present.

Context is typically used by steps to:

- Store values (`name`, `year`, etc.)
- Make branching decisions
- Build dynamic keyboards/messages

---

### 4. Defining menu sections

To define bot flows, you implement `BotMenuSectionProvider`:

```java
import link.buzalex.api.BotMenuSectionProvider;
import link.buzalex.models.UserContextImpl;
import link.buzalex.models.menu.MenuSection;

@Component
public class MyMenuSectionProvider implements BotMenuSectionProvider<UserContextImpl> {

    @Override
    public MenuSection provideMenuSection() {
        return MenuSectionBuilder
                .name("start")
                .selector(msg -> "/start".equals(msg.text()))
                .steps(firstStep())
                .build();
    }

    private BotStepBuilder firstStep() {
        return BotStepBuilder
                .name("askName")
                .message("What's your name?")
                .waitAnswer()
                .saveAs("name")
                .nextStep(greetUser());
    }

    private BotStepBuilder greetUser() {
        return BotStepBuilder
                .name("greetUser")
                .message(ctx -> {
                    String name = ctx.context().getData().get("name");
                    return BotMessageReply.builder()
                            .text("Hello, " + name + "!")
                            .build();
                })
                .finish();
    }
}
```

**Key ideas:**

- `MenuSectionBuilder.name("start")` – defines a logical section.
- `.selector(msg -> "/start".equals(msg.text()))` – **entry condition** for the section (which messages start this flow).
- `.steps(firstStep())` – root step builder; the builder recursively collects all reachable steps.

The library also ships with an example provider: `ExampleGreetingBotMenuSectionProvider`, which demonstrates a more complex flow (name + birth date with interactive keyboards and navigation).

---

### 5. Building steps with `BotStepBuilder`

`BotStepBuilder` is a fluent DSL for describing what happens at each step:

- **Outgoing messages** (static or dynamic)
- **Side‑effects** (`peek` on incoming/outgoing context)
- **Answer handling** and **branching** (`waitAnswer`, `ifTrue`, `saveAs`)
- **Navigation** between steps (`nextStep`, `finish`)
- **Clearing previous messages** (`clearLast`)

Some of the most important methods:

- **`message(String)` / `message(BotMessageReply)` / `message(Function<UserMessageContainer, BotMessageReply>)`**  
  Define what messages the bot sends when the step is executed.

- **`peek(Consumer<UserMessageContainer>)`**  
  Perform side‑effects on the context/message without sending anything yet (e.g. initialize context fields).

- **`waitAnswer()`**  
  Switches into **answer mode**, returning an `AnswerActionsBuilder` where you can:
  - **`saveAs(String)`** – save the incoming answer into the context map.
  - **`ifTrue(Predicate<UserMessageContainer>)`** – add conditional branches with their own messages and next steps.
  - **`nextStep(...)` / `finish()`** – control navigation after handling the answer.
  - **`clearLast()`** – remove the previous message before sending a new one.

- **`nextStep(BotStepBuilder)` / `nextStep(String)` / `finish()`**  
  Navigation between steps. Using the builder variant will automatically collect the entire graph when used inside `MenuSectionBuilder.steps(...)`.

- **`clearLast()`** (available on both step and answer builders)  
  Marks that the previously sent message should be deleted before sending a new one (handled by the underlying manager + `BotApiService.clear`).

The example implementation in `ExampleGreetingBotMenuSectionProvider` demonstrates:

- Paging through years with `<` / `>` buttons (`minus()` / `plus()` steps)
- Month and day selection using dynamically generated keyboards
- Final text output using all collected data from the user context

---

### 6. Sending messages manually with `BotApiService`

Although most interactions are expected to go through the menu/step DSL, you can also send messages manually via `BotApiService`:

```java
import link.buzalex.api.BotApiService;
import link.buzalex.models.BotMessageReply;
import org.springframework.stereotype.Service;

@Service
public class SomeService {
    private final BotApiService botApiService;

    public SomeService(BotApiService botApiService) {
        this.botApiService = botApiService;
    }

    public void notifyUser(Long chatId, String text) {
        botApiService.sendToUser(
                BotMessageReply.builder().text(text).build(),
                chatId
        );
    }
}
```

Available methods:

- **`sendToUser(BotMessageReply message, Long id)`** – send a message with optional keyboard.
- **`sendToUser(String message, Long id, String parseMode)`** – send raw text with a specific parse mode (e.g. `Markdown` or `HTML`).
- **`clear(int messageId, Long chatId)`** – delete a previously sent message.

The starter wires an internal executor that uses `executeAsync` on the underlying `TelegramLongPollingBot` / `TelegramWebhookBot`.

---

### 7. Long‑polling vs Webhook

The library provides two handlers:

- **Long polling**: `TelegramLongPollingHandler`
  - Active when `telegram.bot.type = longPolling` (or property is missing)
  - Extends `TelegramLongPollingBot`
  - Converts each `Update` into `BotMessage`, then delegates to the menu manager

- **Webhook**: `TelegramWebhookHandler`
  - Active when `telegram.bot.type = webhook`
  - Extends `TelegramWebhookBot`
  - On startup:
    - Registers webhook via `SetWebhook` using `telegram.bot.webhook.url + telegram.bot.webhook.path`
  - `BotWebhookRestController` exposes:
    - `POST ${telegram.bot.webhook.path}` which forwards updates to the handler

Switching modes is as simple as changing the `telegram.bot.type` property and providing proper webhook configuration for Telegram when in webhook mode.

---

### 8. Overriding defaults & extension points

You can override or extend behavior by providing your own Spring beans. Many internal implementations are annotated with `@ConditionalOnMissingBean`, for example:

- **`UserContextStorage`**
  - Default: `InMemoryUserContextStorageImpl`
  - Override by defining your own `@Component` or `@Bean` of type `UserContextStorage` (for database, Redis, etc.).

- **`BotMenuSectionProvider`**
  - Default: `ExampleGreetingBotMenuSectionProvider`
  - The example implementation is only used when no other `BotMenuSectionProvider` is present.
  - Provide your own implementations to define real bot flows.

- **Exception handling & message mapping**
  - `ExceptionHandler` and `BotMessageConverter` abstractions allow you to plug in custom behavior if needed (e.g. richer logging, Sentry, etc.).

---

### 9. Example: minimal bot application

```java
@SpringBootApplication
public class MyTelegramBotApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyTelegramBotApplication.class, args);
    }
}
```

With:

- The dependency on `telegram-bot-starter`
- Proper `telegram.bot.*` configuration
- At least one custom `BotMenuSectionProvider` bean

…you will have a fully working Telegram bot with a context‑aware conversational flow.

---

### 10. License & contributions

This project is intended as a reusable starter for building Telegram bots with Spring Boot.  
Feel free to open issues or pull requests in the GitHub repository if you find bugs or have ideas for improvements.

