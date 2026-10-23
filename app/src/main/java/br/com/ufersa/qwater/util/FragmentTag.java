package br.com.ufersa.qwater.util;

/**
 * Tags usadas nas transações de fragmento.
 *
 * A MainActivity compara a tag do fragmento exibido com {@link #HOME} para
 * decidir se o botão voltar retorna à tela inicial. Os fragments de lista usam
 * as mesmas tags ao abrir o formulário pelo botão flutuante.
 */
public final class FragmentTag {

    private FragmentTag() {
    }

    public static final String HOME = "home";
    public static final String SOURCE_FORM = "sourceForm";
    public static final String REPORT_FORM = "reportForm";
    public static final String SOURCE_LIST = "sourceList";
    public static final String REPORT_LIST = "reportList";
    public static final String SOURCE_EDIT = "sourceEdit";
    public static final String REPORT_EDIT = "reportEdit";
}
