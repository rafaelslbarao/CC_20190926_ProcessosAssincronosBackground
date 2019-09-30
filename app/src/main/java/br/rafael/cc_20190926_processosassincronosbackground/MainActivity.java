package br.rafael.cc_20190926_processosassincronosbackground;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button btExecucaoThread;
    private Button btExecucaoAsyncTask;
    private TextView tvProgressoThread;
    private TextView tvProgressoAsyncTask;
    //
    private Handler handlerThreadPrincipal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inicializaComponentes();
    }

    private void inicializaComponentes() {
        btExecucaoThread = findViewById(R.id.btExecucaoThread);
        btExecucaoAsyncTask = findViewById(R.id.btExecucaoAsyncTask);
        tvProgressoThread = findViewById(R.id.tvProgressoThread);
        tvProgressoAsyncTask = findViewById(R.id.tvProgressoAsyncTask);
        //
        btExecucaoThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executaThread();
            }
        });
        //
        handlerThreadPrincipal = new Handler();
        //
        btExecucaoAsyncTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executaAsyncTask();
            }
        });
    }

    private void executaThread() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                for (int i = 0; i <= 100; i++) {
                    final int valorFinal = i;
                    handlerThreadPrincipal.post(new Runnable() {
                        @Override
                        public void run() {
                            tvProgressoThread.setText(valorFinal + "%");
                        }
                    });

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.setName("MINHA THREAD");
        thread.start();
    }

    private void executaAsyncTask()
    {
        MinhaAsyncTask minhaAsyncTask = new MinhaAsyncTask();
        //
        minhaAsyncTask.execute();
    }

    private class MinhaAsyncTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvProgressoAsyncTask.setText("Iniciando");
        }

        @Override
        protected Boolean doInBackground(String... parametros) {
            for (int i = 0; i <= 100; i++) {
                Integer progresso = i;
                publishProgress(progresso);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }

        @Override
        protected void onProgressUpdate(Integer... progresso) {
            super.onProgressUpdate(progresso);
            Integer valorProgresso = progresso[progresso.length-1];
            tvProgressoAsyncTask.setText(valorProgresso + "%");
        }

        @Override
        protected void onPostExecute(Boolean resultado) {
            super.onPostExecute(resultado);
            if(resultado)
                tvProgressoAsyncTask.setText("Finalizou com sucesso");
            else
                tvProgressoAsyncTask.setText("Falhou");
        }
    }


}
