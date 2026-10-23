# Cálculos

Todas as fórmulas e faixas vêm do código original. A refatoração trocou nomes e
separou a classificação do cálculo, sem alterar coeficientes nem limites.

## Unidades

As concentrações são tratadas em mmolc/L nas fórmulas da RAS, como no original.
O seletor de unidades do formulário aceita mmolc/L, mg/L e mEq/L, mas a conversão
não é aplicada antes do cálculo; essa é uma pendência registrada adiante.

A condutividade elétrica é tratada em dS/m. O seletor aceita dS/m, mS/cm e
µS/cm, e apenas µS/cm é convertido, pela divisão por 1000.

## RAS normal

```text
RAS = Na / raiz((Ca + Mg) / 2)
```

Com Ca 4, Mg 2 e Na 10, o resultado é 5,7735. O valor é arredondado para cima,
com quatro casas decimais, antes de ser classificado.

## RAS corrigida

O cálcio informado é substituído pelo cálcio corrigido, obtido da tabela do
apêndice 2 do livro *A qualidade da água para irrigação*. A tabela cruza a razão
HCO3/Ca, de 0,05 a 30, com a condutividade elétrica, de 0 a 8 dS/m, e a
interpolação é linear dupla.

```text
Ca corrigido = tabela(HCO3 / Ca, CE)
RAS* = Na / raiz((Ca corrigido + Mg) / 2)
```

Fora da faixa da tabela, o resultado é NaN, e a RAS corrigida não é apresentada.

## Classes de risco

| Parâmetro | Faixa | Classe | Risco |
|---|---|---|---|
| Salinidade (CE) | menor que 0,25 | C1 | OK |
| | 0,25 a 0,74 | C2 | Atenção |
| | 0,75 a 2,24 | C3 | Alerta |
| | 2,25 a 3,99 | C4 | Alerta |
| | 4,00 a 5,99 | C5 | Perigo |
| | 6,00 ou mais | C6 | Perigo |
| Infiltração (RAS*) | menor que 10 | S1 | OK |
| | 10 a 17,99 | S2 | Atenção |
| | 18 a 25,99 | S3 | Alerta |
| | 26 ou mais | S4 | Perigo |
| Cloreto | até 2 | CL1 | OK |
| | até 4 | CL2 | Atenção |
| | até 10 | CL3 | Alerta |
| | acima de 10 | CL4 | Perigo |
| Boro | até 0,5 | B1 | OK |
| | até 1,0 | B2 | Atenção |
| | até 2,0 | B3 | Alerta |
| | acima de 2,0 | B4 | Perigo |
| Sódio, superfície | menor que 3 | SS1 | OK |
| | 3 a 9 | SS2 | Atenção |
| | acima de 9 | SS3 | Perigo |
| Sódio, aspersão | até 3 | SA1 | OK |
| | acima de 3 | SA2 | Atenção |
| pH | menor que 7 | PH1 | OK |
| | 7 a 8 | PH2 | Atenção |
| | acima de 8 | PH3 | Perigo |

## Coerência iônica

```text
erro = |(cátions - ânions) / (cátions + ânions)| * 100
```

| Erro | Classe | Risco |
|---|---|---|
| menor que 5% | CO1 | OK |
| 5% a 10% | CO2 | Atenção |
| acima de 10% | CO3 | Perigo |

A coerência só é calculada quando os oito íons estão informados. Soma zero
resulta em CO3, como no original.

## Ícone de toxidez

Os ícones de cloreto, boro e sódio não têm ícone próprio. O ícone da seção de
toxidez mostra o maior risco entre os parâmetros informados:

```text
ordem de gravidade: OK < Atenção < Alerta < Perigo
```

## Conversões de unidade

| Conversão | Fórmula |
|---|---|
| mEq/L para mg/L | `mEq/L * (massa / valência)` |
| mg/L para mEq/L | `mg/L / (massa / valência)` |
| mmol/L para mEq/L | `mmol/L * valência` |
| mEq/L para mmol/L | `mEq/L / valência` |
| mg/L para mmol/L | `(mg/L / valência) / massa` |

Massas molares usadas: Ca 40,08; Mg 24,31; K 39,10; Na 29,99; CO3 60,0089;
HCO3 61,0168; Cl 35,453.

## Pendências

1. A massa do sódio é 29,99 g/mol no código original, e não 22,99 g/mol. O valor
   foi preservado. A correção muda resultados do relatório e depende de decisão
   do orientador.
2. A conversão de mg/L para mmol/L divide pela valência, o que difere da
   conversão usual, em que apenas a massa molar é usada. A fórmula foi
   preservada e coberta por teste.
3. O seletor de unidades dos íons não é aplicado ao cálculo. A seleção é lida,
   mas os valores seguem para as fórmulas como se estivessem em mmolc/L.
4. A linha de cabeçalho da tabela de cálcio corrigido, que guarda as
   condutividades, é usada como linha de dados pela busca original. Com isso, a
   razão HCO3/Ca igual a 0,05 é interpolada entre o cabeçalho e a primeira linha
   e o resultado fica 13,2, em vez dos 13,61 da célula. O comportamento foi
   preservado e coberto por teste.
5. A tabela de cálcio corrigido termina em 8 dS/m e em razão 30. Valores acima
   disso não têm classificação de infiltração.
6. A quantidade de sólidos em suspensão e a obstrução de gotejadores, citadas no
   texto de apresentação, não são calculadas.
