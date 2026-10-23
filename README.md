# QWATER

Apoio à decisão sobre a qualidade da água para irrigação: salinidade,
infiltração, toxidez de cloreto, boro e sódio, pH e coerência iônica. O cálculo
é o do projeto original de Arlan Medeiros, preservado.

Aplicativo Android com banco local: as fontes de água são georreferenciadas e os
relatórios ficam datados no aparelho, sem internet.

## Abrir

- Repositório: <https://github.com/pwebufersa/QWater>
- APK de depuração: [dist/app-debug.apk](dist/app-debug.apk)
- Original, por registro histórico: <https://github.com/ArlanMDs/Qwater>

O APK é de depuração porque o projeto não traz chave de assinatura de release.

## Integração contínua

[![Aplicativo Android](https://github.com/pwebufersa/QWater/actions/workflows/aplicativo-android.yml/badge.svg?branch=master)](https://github.com/pwebufersa/QWater/actions/workflows/aplicativo-android.yml)

- **Testes automáticos**: `./gradlew test` em cada push e em cada pull request.
- **Aplicativo Android**: gera o APK em cada mudança de código, publica o
  artefato `qwater-apk` com hash e versão e substitui `dist/app-debug.apk` na
  branch `master`.

O fluxo do APK usa `ubuntu-24.04`, JDK 21 e a plataforma 34 do SDK Android. A
versão publicada é a declarada em `app/build.gradle`.

## Usar

1. A fonte é cadastrada em **Register Source**, com nome, tipo e, se desejado,
   coordenadas obtidas do GPS.
2. Os parâmetros da amostra são informados em **Report**, com cátions, ânions,
   condutividade elétrica, pH e boro.
3. O botão **Assess the quality** abre a análise, com as classificações e os
   ícones de risco.
4. O relatório é salvo em **Save**, com data e fonte.
5. Em **Reports** e **Sources**, os registros salvos são listados; cada linha
   abre os detalhes, com edição e exclusão.

O texto da interface está em inglês, como no original.

## Campos

| Campo | Unidade | Observação |
|---|---|---|
| Ca²⁺, Mg²⁺, K⁺, Na⁺ | mmolc/L, mg/L ou mEq/L | unidade escolhida no seletor |
| CO₃²⁻, HCO₃⁻, Cl⁻, SO₄²⁻ | mmolc/L, mg/L ou mEq/L | unidade escolhida no seletor |
| CEw | dS/m, mS/cm ou µS/cm | µS/cm é convertido para dS/m |
| Boro | ppm | opcional |
| pH | adimensional | opcional |

Campo vazio é lido como zero. A vírgula decimal é recusada; o separador é o
ponto, como avisa a mensagem de erro do formulário.

## Cálculos

RAS, RAS corrigida e faixas de classificação estão descritos em
[docs/calculos.md](docs/calculos.md), com a origem de cada fórmula.

## Testes

```bash
./gradlew test
```

Os testes de JVM cobrem a interpolação da tabela de cálcio corrigido, as cinco
conversões de unidade, o cálculo da RAS e todas as faixas de classificação. O
teste de instrumentação confere o pacote do aplicativo e exige aparelho ou
emulador:

```bash
./gradlew connectedAndroidTest
```

## Compilar

Requisitos: JDK 21 e Android SDK com a plataforma 34 e as build tools 34.0.0.
O caminho do SDK é declarado em `local.properties`:

```properties
sdk.dir=C\:\\Android\\sdk
```

O build é o do projeto original, com o wrapper do Gradle, sem tarefa auxiliar:

```bash
export JAVA_HOME=/c/Users/Xico/jdk21
export ANDROID_HOME=/c/Android/sdk
./gradlew assembleDebug
```

Saída em `app/build/outputs/apk/debug/app-debug.apk`. A cópia publicada em
`dist/app-debug.apk` é substituída a cada entrega.

Configuração preservada: AGP 8.6.0, Gradle 8.14.3, `compileSdkVersion 34`,
`minSdkVersion 21`, `targetSdkVersion 34`, versão 1.1.0, código 22. O
`BuildConfig` é gerado porque `buildFeatures { buildConfig true }` está
declarado.

## Estrutura

- `app/src/main/java/br/com/ufersa/qwater/activities/`: telas de navegação,
  análise, detalhes e salvamento.
- `app/src/main/java/br/com/ufersa/qwater/fragments/`: formulários e listas.
- `app/src/main/java/br/com/ufersa/qwater/adapters/`: linhas das listas.
- `app/src/main/java/br/com/ufersa/qwater/beans/`: entidades do Room.
- `app/src/main/java/br/com/ufersa/qwater/database/`: banco e consultas.
- `app/src/main/java/br/com/ufersa/qwater/util/`: cálculo, classificações e
  contratos de Intent.
- `app/src/main/java/br/com/ufersa/qwater/help/`: telas de explicação.
- `app/src/test/`: testes de JVM.
- `dist/`: APK de depuração da última entrega.

O detalhamento está em [docs/arquitetura.md](docs/arquitetura.md).

## Autoria

Original: [Arlan Medeiros](https://github.com/ArlanMDs) · Adaptação e build:
[Francisco de Assis P. V. de Arruda](https://sigaa.ufersa.edu.br/sigaa/public/docente/portal.jsf?siape=1817174),
UFERSA.
