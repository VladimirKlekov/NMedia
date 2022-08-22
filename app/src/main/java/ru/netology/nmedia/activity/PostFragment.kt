package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg
import ru.netology.nmedia.adapter.PostEventListener
import ru.netology.nmedia.adapter.PostViewHolder
import ru.netology.nmedia.databinding.FragmentPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PostFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PostFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
//________________________________________________________________________________________________//

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )
//________________________________________________________________________________________________//
    private val args by navArgs<PostFragmentArgs>()
//________________________________________________________________________________________________//
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
//________________________________________________________________________________________________//
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPostBinding.inflate(inflater, container, false)

//_______________________________________________________________________________________________//

        val viewHolder = PostViewHolder(binding.cardPost, object : PostEventListener {
            override fun onEdit(post: Post) {
                //вызвал метод редактирования из вью модели с данными поста
                viewModel.edit(post)
                //свернул пост фрагмент
                findNavController().navigate(
                    //при этом перешол к нью пост фрагмент
                    R.id.action_postFragment_to_newPostFragment,
                    //выполнил и сохранил в конструктор
                    Bundle().apply {
                        textArg = post.content
                   }
                )
            }
//________________________________________________________________________________________________//
            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }
//________________________________________________________________________________________________//
            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }
//________________________________________________________________________________________________//
            override fun onShare(post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(intent, "Share post")
                startActivity(shareIntent)
                viewModel.shareById(post.id)
            }
//________________________________________________________________________________________________//
            override fun onVideo(post: Post) {
                val intentVideo = Intent(Intent.ACTION_VIEW, Uri.parse(post.video))
                startActivity(intentVideo)
            }
//________________________________________________________________________________________________//
            override fun onPost(post: Post) {
            }
        })
//_______________________________________________________________________________________________//
        //подписываемся на обновление списка
        viewModel.data.observe(viewLifecycleOwner) { posts ->
            //ищем пост, который нужно отобразить
            val post = posts.find { it.id == args.postId.toLong() } ?: run {
                findNavController().navigateUp()
                return@observe
            }
            viewHolder.bind(post)
        }
        return binding.root
    }
//________________________________________________________________________________________________//
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PostFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
        //метод newInstanсе нужен, что бы сохранить логику создания нового объекта
            //вернет класс PostFragment
            PostFragment().apply {
                arguments = Bundle().apply {
                    //у которого в свойсвах аргумента записано две строчки
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                    //плучается, что мы вне класса PostFragment не вызваем конструктор
                }
            }
    }
}