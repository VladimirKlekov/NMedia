package ru.netology.nmedia.activity


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.viewmodel.PostViewModel
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


class NewPostFragment : Fragment() {

    companion object {
        var Bundle.textArg: String? by StringArg
    }

    val args by navArgs<NewPostFragmentArgs>()

    //____________________________________________________________________________________________//
    //представляем ViewModel нескольким активити
    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )
    //____________________________________________________________________________________________//

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentNewPostBinding = FragmentNewPostBinding.inflate(
            inflater,
            container,
            false
        )
        arguments?.textArg?.let {
            binding.content.setText(it)
        }
//_______________________________________________________________________________________________//
        var backText = viewModel.textStorageDelete()

        if (backText != null) {
            binding.content.setText(backText).toString()
            backText = ""
        } else {
            //Обращаемся к свойсту аргумента и читаем текст.
            arguments?.textArg?.let {
                binding.content.setText(it)
            }
        }
//_______________________________________________________________________________________________//
        binding.content.requestFocus()
//_______________________________________________________________________________________________//
        //скрытие подсказок, если пост без текста
        if (arguments?.textArg == null) {
            binding.supportGroup.isGone = true
        }
        //сохранение текста при возврате
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            var copyText = binding.content.text.toString()
            viewModel.textStorage(copyText)
            findNavController().navigateUp()
        }

//_________________________________________________________________________________________________//
        //оформление

        //Заголовок редактируемого поста
        binding.supportEdit.text = binding.content.text
        // закрытие (отмена) редактирования
        binding.closeEditidButton.setOnClickListener {
            findNavController().navigateUp()
        }
//_________________________________________________________________________________________________//
        //сохранение поста
                binding.save.setOnClickListener {
            viewModel.editContent(binding.content.text.toString())
            viewModel.save()
            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigateUp()

        }
//_________________________________________________________________________________________________//
        return binding.root

    }


    object StringArg : ReadWriteProperty<Bundle, String?> {
        override fun getValue(thisRef: Bundle, property: KProperty<*>): String? {
            return thisRef.getString(property.name)
        }

        override fun setValue(thisRef: Bundle, property: KProperty<*>, value: String?) {
            thisRef.putString(property.name, value)
        }

    }
}