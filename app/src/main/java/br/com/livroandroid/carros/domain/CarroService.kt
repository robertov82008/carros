package br.com.livroandroid.carros.domain

import android.content.Context
import android.util.Log
import br.com.livroandroid.carros.R
import br.com.livroandroid.carros.extensions.getText
import br.com.livroandroid.carros.extensions.getXml

object CarroService {
    private val TAG = "livro"

    // Busca os carros por tipo (clássicos, esportivos ou luxo)
    fun getCarros(context: Context, tipo: TipoCarro): List<Carro> {
        // Este é o arquivo que temos que ler
        val raw = getArquivoRaw(tipo)

        // Abre o arquivo para leitura
        val resources = context.resources
        val inputStream = resources.openRawResource(raw)
        inputStream.bufferedReader().use {
            // Lê o XML e cria a lista de carros
            val xml = it.readText()
            return parserXML(xml)
        }
    }

    // Retorna o arquivo que tempo que ler para o tipo informado
    fun getArquivoRaw(tipo: TipoCarro) = when(tipo) {
        TipoCarro.classicos -> R.raw.carros_classicos
        TipoCarro.esportivos -> R.raw.carros_esportivos
        else -> R.raw.carros_luxo
    }

    // Lê o XML e cria a lista de carros
    fun parserXML(xmlString: String): List<Carro> {
        val carros = mutableListOf<Carro>()
        val xml = xmlString.getXml()
        // Lê todas as tags <carro>
        val nodeCarros = xml.getChildren("carro")
        // Insere cada carro na lista
        for (node in nodeCarros) {
            val c = Carro()
            // Lê as informações de cada carro
            c.nome = node.getText("nome")
            c.desc = node.getText("desc")
            c.urlFoto = node.getText("url_foto")
            carros.add(c)
        }
        Log.d(TAG, "${carros.size} carros encontrados.")
        return carros
    }
}