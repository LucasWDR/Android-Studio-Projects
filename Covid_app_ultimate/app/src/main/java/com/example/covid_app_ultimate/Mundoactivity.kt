package com.example.covid_app_ultimate
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.mundo.*

import java.text.DecimalFormat

class MundoActivity : AppCompatActivity() {

    private var estatisticasList = mutableListOf<Estatisticas>()
    private var asyncTask: EstatisticasTask? = null
    val acumulador = Estatisticas(
        country = null,
        uf = null,
        state = null,
        suspects = 0,
        refuses = 0,
        cases = 0,
        confirmed = 0,
        deaths = 0,
        recovered = 0,
        date = null,
        hour = null
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mundo.xml)

        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = ""
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        carregarEstatisticas()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun carregarEstatisticas() {
        estatisticasList.clear()
        if (estatisticasList.isNotEmpty()) {
            showProgress(false)
        } else {
            if (asyncTask == null) {
                if (EstatisticasHTTP.hasConnection(this)) {
                    if (asyncTask?.status != AsyncTask.Status.RUNNING) {
                        asyncTask = EstatisticasTask()
                        asyncTask?.execute()
                    } else {
                        progress.visibility = View.GONE
                        Toast.makeText(this, "Sem conexão!", Toast.LENGTH_LONG).show()
                    }
                }
            } else if (asyncTask?.status == AsyncTask.Status.RUNNING) {
                showProgress(true)
            }
        }
    }

    inner class EstatisticasTask : AsyncTask<Void, Void, List<Estatisticas>>() {
        override fun onPreExecute() {
            super.onPreExecute()
            showProgress(true)
        }

        override fun doInBackground(vararg params: Void?): List<Estatisticas>? {
            val path = "/countries"
            return EstatisticasHTTP.loadEstatisticas(path)
        }

        override fun onPostExecute(resultado: List<Estatisticas>?) {
            super.onPostExecute(resultado)
            showProgress(false)
            atualizarEstatisticas(resultado)
        }
    }

    private fun atualizarEstatisticas(resultado: List<Estatisticas>?) {
        if (resultado != null) {
            this.estatisticasList.clear()
            this.estatisticasList.addAll(resultado)
            acumularDados()
            exibirDados()
        }
    }

    fun showProgress(show: Boolean) {
        if (!show) {
            progress.visibility = if (show) View.VISIBLE else View.GONE
        }
    }

    fun acumularDados() {
        for (index in 0..estatisticasList.size - 1) {
            val pais = estatisticasList[index]
            acumulador.cases = acumulador.cases + pais.cases

            acumulador.confirmed = acumulador.confirmed + pais.confirmed
            acumulador.deaths = acumulador.deaths + pais.deaths
            acumulador.recovered = acumulador.recovered + pais.recovered
        }
    }

    fun exibirDados() {
        val df = DecimalFormat("###,###")
        view_casesResult.text = df.format(acumulador.cases)

        view_comfirmados.text = df.format(acumulador.confirmed)
        viewrecuperados.text = df.format(acumulador.recovered)
        viewmortes.text = df.format(acumulador.deaths)
        textView_title.text = "Estatísticas \n Mundiais:"
    }
}