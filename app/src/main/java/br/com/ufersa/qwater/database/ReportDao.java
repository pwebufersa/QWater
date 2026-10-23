package br.com.ufersa.qwater.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.com.ufersa.qwater.beans.Report;

/**
 * Acesso aos relatórios de análise.
 *
 * Exemplo: a lista de relatórios é obtida por {@link #getAll()} e filtrada por
 * fonte por {@link #getReportsBySource(int)}.
 */
@Dao
public interface ReportDao {

    @Query("SELECT * FROM Report")
    List<Report> getAll();

    @Insert
    void insertAll(List<Report> reports);

    @Insert
    void insert(Report report);

    @Update
    void update(Report report);

    @Delete
    void delete(Report report);

    @Query("SELECT * FROM Report WHERE souId IS :sourceId")
    List<Report> getReportsBySource(int sourceId);
}
