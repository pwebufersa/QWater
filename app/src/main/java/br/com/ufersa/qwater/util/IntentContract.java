package br.com.ufersa.qwater.util;

/**
 * Chaves, códigos de requisição e finalidades trocados entre activities e
 * fragments por Intent e por Bundle.
 *
 * A finalidade diz o que a tela chamadora espera: editar um relatório, apenas
 * visualizá-lo, ou voltar para uma lista depois de excluir um registro.
 *
 * Exemplo: ao abrir a MainActivity para editar um relatório, o extra
 * {@link Key#PURPOSE} recebe {@link Purpose#EDIT_REPORT} e o extra
 * {@link Key#REPORT} recebe o relatório.
 */
public final class IntentContract {

    private IntentContract() {
    }

    /** Nomes dos extras de Intent e de Bundle. */
    public static final class Key {

        private Key() {
        }

        public static final String PURPOSE = "purpose";
        public static final String CALLER = "caller";
        public static final String REPORT = "report";
        public static final String SOURCE = "source";
        public static final String SOURCE_ID = "sourceId";
        public static final String SOURCE_NAME = "sourceName";
        public static final String REPORT_DATE = "reportDate";
    }

    /** O que a tela de destino deve fazer ao ser aberta. */
    public static final class Purpose {

        private Purpose() {
        }

        public static final int EDIT_REPORT = 1;
        public static final int VIEW_REPORT = 2;
        public static final int REPORT_DELETED = 3;
        public static final int SOURCE_DELETED = 4;
        public static final int EDIT_SOURCE = 5;
        public static final int SOURCE_UPDATED = 6;
    }

    /** Códigos de requisição de startActivityForResult. */
    public static final class Request {

        private Request() {
        }

        public static final int SAVE_NEW_REPORT = 1;
        public static final int PICK_SOURCE = 2;
        public static final int SAVE_UPDATED_REPORT = 3;
        public static final int RESOLVE_LOCATION_SETTINGS = 100;
    }

    /** Activities que podem abrir a lista de fontes. */
    public static final class Caller {

        private Caller() {
        }

        public static final int MAIN = 0;
        public static final int REPORT_SAVE = 1;
    }

    /** Nomes das chaves de estado usadas ao recriar a tela de fonte. */
    public static final class StateKey {

        private StateKey() {
        }

        public static final String REQUESTING_LOCATION_UPDATES = "requestingLocationUpdates";
        public static final String LAST_KNOWN_LOCATION = "lastKnownLocation";
    }
}
