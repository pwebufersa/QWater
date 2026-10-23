# Arquitetura

## Camadas

| Pacote | Papel |
|---|---|
| `activities` | navegação, análise, detalhes, salvamento e seleção de fonte |
| `fragments` | formulários de fonte e de relatório, listas de fontes e de relatórios, tela inicial |
| `adapters` | linhas das listas de fontes e de relatórios |
| `beans` | entidades do Room, também usadas como Parcelable entre telas |
| `database` | banco local e consultas |
| `util` | cálculo, classificação, contratos de Intent e tags de fragmento |
| `help` | telas de explicação dos parâmetros |

Não há camada de repositório: as activities e os fragments usam o DAO
diretamente, como no original. A alteração de escopo para uma camada de
repositório não fazia parte da refatoração.

## Fluxo de telas

1. `SplashActivity` abre `MainActivity` após dois segundos.
2. `MainActivity` hospeda os fragmentos e o menu lateral: início, cadastro de
   fonte, relatório, lista de relatórios e lista de fontes.
3. `SourceFormFragment` insere ou atualiza a fonte. Ao inserir, abre
   `ReportFormFragment`; ao atualizar, volta para `MainActivity` com a
   finalidade `SOURCE_UPDATED`.
4. `ReportFormFragment` monta o `Report` e abre `ReportAnalysisActivity`.
5. `ReportAnalysisActivity` calcula a RAS e apresenta as classificações. O
   salvamento passa por `ReportSaveActivity`, que devolve data e fonte.
6. `ReportDetailsActivity` e `SourceDetailsActivity` exibem os registros e
   permitem editar ou excluir.
7. `SourcePickerActivity` atende apenas o salvamento do relatório, porque o
   fragmento de lista da MainActivity não devolve resultado.

## Contratos entre telas

Os extras, os códigos de requisição e as finalidades ficam em
`util/IntentContract`:

| Grupo | Uso |
|---|---|
| `Key` | nomes dos extras: `PURPOSE`, `REPORT`, `SOURCE`, `SOURCE_ID`, `SOURCE_NAME`, `REPORT_DATE`, `CALLER` |
| `Purpose` | o que a tela de destino deve fazer: `EDIT_REPORT`, `VIEW_REPORT`, `REPORT_DELETED`, `SOURCE_DELETED`, `EDIT_SOURCE`, `SOURCE_UPDATED` |
| `Request` | códigos de `startActivityForResult` |
| `Caller` | quem abriu a lista de fontes: `MAIN` ou `REPORT_SAVE` |
| `StateKey` | chaves de estado da tela de fonte |

As tags de fragmento ficam em `util/FragmentTag`, usadas pela MainActivity e
pelos botões flutuantes das listas. A comparação da tag `HOME` decide o
comportamento do botão voltar.

## Modelo de dados

Banco `appDatabase.db`, versão 1, com migração destrutiva: uma mudança de
esquema apaga o banco anterior.

| Entidade | Colunas |
|---|---|
| `Source` | `id`, `name` (único), `type`, `latitude`, `longitude` |
| `Report` | `id`, `ca`, `mg`, `k`, `na`, `co3`, `hco3`, `cl`, `so4`, `b`, `cea`, `ph`, `normalSar`, `correctedSar`, `date`, `souId`, `souName` |

Os atributos de `Report` têm nomes por extenso, e a coluna correspondente é
fixada por `@ColumnInfo`, de modo que o esquema da versão 1 permanece idêntico.
A chave estrangeira de `Report` aponta para `Source` por identificador e por
nome, com exclusão em cascata.

## Cálculo e classificação

`util/WaterQualityClassifier` concentra as faixas de classificação e devolve um
rótulo, que é o nome do texto em `res/values/strings.xml`, e um `RiskLevel`. A
tela de análise apenas traduz o rótulo em texto e o risco em ícone. Essa
separação permite testar as faixas sem aparelho.

`util/SarCalculator` calcula a RAS normal e a corrigida. A tabela de cálcio
corrigido fica em `util/CorrectedCalciumTable`, com interpolação linear dupla.
`util/IonUnitConverter` converte as unidades a partir do enum `Ion`, que guarda
massa molar e valência.

## Nomes adotados

| Antes | Depois |
|---|---|
| `AppDatabase` | `QWaterDatabase` |
| `AnalyzeReportActivity` | `ReportAnalysisActivity` |
| `SaveReportActivity` | `ReportSaveActivity` |
| `ListSourcesActivity` | `SourcePickerActivity` |
| `CreateSourceFragment` | `SourceFormFragment` |
| `CreateReportFragment` | `ReportFormFragment` |
| `ListSourcesFragment` | `SourceListFragment` |
| `ListReportsFragment` | `ReportListFragment` |
| `SourceAdapter` | `SourceListAdapter` |
| `ReportAdapter` | `ReportListAdapter` |
| `CaTable` | `CorrectedCalciumTable` |
| `UnitConverter` | `IonUnitConverter` |
| `SARCalculator` | `SarCalculator` |
| `Flags` | `IntentContract` e `FragmentTag` |
| `info_activities.ToxityActivity` | `help.ToxicityActivity` |
| `info_activities` | `help` |

Campos e métodos também foram renomeados: `ca` virou `calcium`, `souId` virou
`sourceId`, `mFusedLocationClient` virou `fusedLocationClient`, `AsyncInsert`
virou `InsertReportTask` e `sourceAlreadyExists_onInsert` virou
`isSourceNameTaken`, entre outros.

Os identificadores de recursos (`R.id.EDIT_CA`, `R.layout.activity_toxity`)
foram mantidos, porque a renomeação exigiria alterar todos os layouts sem ganho
de comportamento.

## Testes

| Arquivo | Cobertura |
|---|---|
| `IonUnitConverterTest` | as cinco conversões, ida e volta, para todos os íons |
| `CorrectedCalciumTableTest` | célula exata, média de quatro células, limites e fora de faixa |
| `SarCalculatorTest` | RAS normal, RAS corrigida, zero e fora de faixa |
| `WaterQualityClassifierTest` | limites de todas as classes e coerência iônica |
| `PackageNameInstrumentedTest` | pacote do aplicativo no dispositivo |

A execução é feita com `./gradlew test`. Os testes não dependem de emulador nem
de rede.

## Decisões preservadas

1. `AsyncTask` continua sendo usado, com os avisos de vazamento de memória do
   original mantidos como comentários.
2. A migração destrutiva do banco foi mantida, com o mesmo aviso de que
   migrações devem ser escritas quando a versão mudar.
3. O `BuildConfig` passou a ser gerado por `buildFeatures { buildConfig true }`,
   exigência do AGP 8, sem mudar a versão do plugin nem do Gradle.
4. As telas de ajuda passaram a herdar de `HelpActivity`, que centraliza o botão
   de voltar.

## Pendências

1. Não há chave de release no repositório, por isso o APK publicado é de
   depuração.
2. As conversões do seletor de unidades dos íons não são aplicadas ao cálculo;
   os detalhes estão em [calculos.md](calculos.md).
3. O formato de data é fixo em `dd/MM/yyyy`, sem internacionalização.
4. Os textos da interface estão em inglês.
