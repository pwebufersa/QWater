package br.com.ufersa.qwater.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.com.ufersa.qwater.beans.Source;

/**
 * Acesso às fontes de água cadastradas.
 *
 * Exemplo: {@link #getSourceNames()} alimenta a verificação de nome repetido no
 * formulário de fonte; {@link #getAll()} alimenta a lista e o seletor de fontes.
 */
@Dao
public interface SourceDao {

    @Query("SELECT * FROM Source")
    List<Source> getAll();

    @Query("SELECT name FROM Source")
    String[] getSourceNames();

    @Insert
    void insertAll(List<Source> sources);

    @Insert
    void insert(Source source);

    @Update
    void update(Source source);

    @Delete
    void delete(Source source);
}
