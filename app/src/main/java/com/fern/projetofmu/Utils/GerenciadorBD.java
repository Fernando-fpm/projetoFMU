package com.fern.projetofmu.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fern.projetofmu.model.ToDoModel;

import java.util.ArrayList;
import java.util.List;


public class GerenciadorBD extends SQLiteOpenHelper{

     private static final String NOME_BANCO = "Banco_TAREFAS";
    private static final String TABELA_TAREFAS = "tarefas";
    private static final String ID = "id";
    private static final String TAREFA = "tarefa";
    private static final String STATUS = "status";
    private static final String CREATE_TABELA_TAREFAS =
            "CREATE TABLE " + TABELA_TAREFAS + " (" +
                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TAREFA + " TEXT, " +
                    STATUS + " INTEGER)";

    private static final int VERSAO= 1;
    private SQLiteDatabase db;

    public GerenciadorBD(Context context){
        super(context, NOME_BANCO, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TABELA_TAREFAS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int novaVersao, int versaoAntiga){
        //Apaga as tabelas antigas
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_TAREFAS);
        //Cria as tabelas novamente
        onCreate(db);
    }

    public void openDatabase(){
        db = getWritableDatabase();
    }

    public void InsertTask(ToDoModel task){
        ContentValues cv = new ContentValues();
        cv.put(TAREFA, task.getTask());
        cv.put(STATUS, 0);
        db.insert(TABELA_TAREFAS, null, cv);
    }

    public List<ToDoModel> getAllTasks(){
        List<ToDoModel>taskList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();;
            try{
              cur = db.query(TABELA_TAREFAS, null, null, null, null, null, null, null);
              if (cur != null){
                  if (cur.moveToFirst()){
                      do{
                          ToDoModel task = new ToDoModel();
                          task.setId(cur.getInt(cur.getColumnIndexOrThrow(ID)));
                          task.setTask(cur.getString(cur.getColumnIndexOrThrow(TAREFA)));
                          task.setStatus(cur.getInt(cur.getColumnIndexOrThrow(STATUS)));
                          taskList.add(task);
                      }while (cur.moveToNext());
                  }
              }
            }
            finally {
                db.endTransaction();
                cur.close();
            }
            return taskList;
    }
    public void updateStatus(int id, int status){
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(TABELA_TAREFAS, cv, ID + "=?", new String[] {String.valueOf(id)});
    }
    public void updateTask(int id, String task){
        ContentValues cv = new ContentValues();
        cv.put(TAREFA, task);
        db.update(TABELA_TAREFAS, cv, ID + "+?", new String[] {String.valueOf(id)});
    }
    public void deleteTask(int id){
        db.delete(TABELA_TAREFAS, ID + "=?", new String[] {String.valueOf(id)});
    }
}
