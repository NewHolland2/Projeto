package com.aifarmtech.orgs.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.aifarmtech.orgs.database.AppDataBase
import com.aifarmtech.orgs.databinding.ActivityFormularioCadastroUsuarioBinding
import com.aifarmtech.orgs.extensions.toast
import com.aifarmtech.orgs.model.Usuario
import kotlinx.coroutines.launch

class FormularioCadastroUsuarioActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityFormularioCadastroUsuarioBinding.inflate(layoutInflater)
    }
    private val usuarioDao by lazy {
        AppDataBase.instancia(this).UsuarioDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configuraBotaoCadastrar()
    }

    private fun configuraBotaoCadastrar() {
        binding.activityBotaoCadatrar.setOnClickListener {
            val novoUsuario = criaUsuario()
            Log.i("CadastrarUsuario", "onCreate: $novoUsuario")
            cadastro(novoUsuario)
        }
    }

    private fun cadastro(usuario: Usuario) {
        lifecycleScope.launch {
            try {
                usuarioDao.salva(usuario)
                finish()
            } catch (e: Exception) {
                Log.e("CadastrarUsuario", "configuraBotaoCadastrar: ", e)
                toast("Falha ao cadastrar usuário")
            }
        }
    }

    private fun criaUsuario(): Usuario {
        val usuario = binding.activityFormularioCadastroUsuario.text.toString()
        val nome = binding.activityFormularioCadastroNome.text.toString()
        val senha = binding.activityFormularioCadastroSenha.text.toString()
        return Usuario(usuario, nome, senha)
    }

}