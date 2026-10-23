package br.com.ufersa.qwater.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import br.com.ufersa.qwater.beans.Report;
import br.com.ufersa.qwater.beans.Source;

/**
 * Banco de dados local do aplicativo, com uma instância por processo.
 *
 * A versão 1 usa migração destrutiva: uma mudança de esquema apaga o banco
 * antigo. As migrações devem ser escritas quando a versão for elevada.
 */
@Database(entities = {Report.class, Source.class}, version = 1)
public abstract class QWaterDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "appDatabase.db";
    private static volatile QWaterDatabase instance;

    public static synchronized QWaterDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    // The build just creates the configuration for the database so you can call this from the main thread.
    private static QWaterDatabase create(final Context context) {
        return Room.databaseBuilder(
                context,
                QWaterDatabase.class,
                DATABASE_NAME)
                .fallbackToDestructiveMigration()// esse método destroi a bd antigo se a versão mudar. //TODO fazer métodos de migração quando necessário
                .build();
    }

    public abstract ReportDao reportDao();

    public abstract SourceDao sourceDao();
}
