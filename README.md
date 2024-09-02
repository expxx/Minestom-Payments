
# Minestom Payments

Minestom Payments is a quick and easy library to hook in with your webstore. Currently supports [Tebex](https://tebex.io) and [CraftingStore](https://craftingstore.net), with [Agora](https://agoramp.com) and [MineStoreCMS](https://minestorecms.com) planned.



## Badges

![GitHub License](https://img.shields.io/github/license/expxx/Minestom-Payments?style=for-the-badge)

![GitHub Actions Workflow Status](https://img.shields.io/github/actions/workflow/status/expxx/Minestom-Payments/maven.yml?style=for-the-badge)

![Libraries.io dependency status for GitHub repo](https://img.shields.io/librariesio/github/expxx/Minestom-Payments?style=for-the-badge)

![GitHub Issues or Pull Requests](https://img.shields.io/github/issues/expxx/Minestom-Payments?style=for-the-badge)



## Installation

```xml
<repositories>
    <repository>
        <id>cams-utils-releases</id>
        <url>https://repo.expx.dev/releases</url>
    </repository>
</repositories>

<dependency>
    <groupId>dev.expx.minestom</groupId>
    <artifactId>Payments</artifactId>
    <version>1.2.3</version>
</dependency>
```
    
## Usage

```java
import net.minestom.server.MinecraftServer;
import dev.expx.payments.PaymentHandler;
import dev.expx.payments.StoreType;

public static void main(String[] args) {
    MinecraftServer server = MinecraftServer.init();
    PaymentHandler.init(StoreType.TEBEX_STORE, Path.of("store"));
    server.start("0.0.0.0", 25565)
}

```


## Contributing

Contributions are always welcome!

Open an issue/pr :)

