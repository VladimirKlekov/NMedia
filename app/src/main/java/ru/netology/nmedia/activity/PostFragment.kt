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

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    private val args by navArgs<PostFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentPostBinding.inflate(inflater, container, false)

        val viewHolder = PostViewHolder(binding.cardPost, object : PostEventListener {

            override fun onEdit(post: Post) {
                viewModel.edit(post)
                findNavController().navigate(
                    R.id.action_feedFragment_to_newPostFragment,
                    Bundle().apply {
                        textArg = post.content
                    }
                )
//
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onShare(post: Post) {
                viewModel.shareById(post.id)
            }

            override fun onVideo(post: Post) {
                val intentVideo = Intent(Intent.ACTION_VIEW, Uri.parse(post.video))
                startActivity(intentVideo)
            }

            override fun onPost(post: Post) {

            }

        })
        //подписываемся на обновление списка
        viewModel.data.observe(viewLifecycleOwner){posts->
            //ищем пост, который нужно отобразить

            val post = posts.find { it.id == args.postId.toLong()}?:run {
                findNavController().navigateUp()
                return@observe
            }
            viewHolder.bind(post)
        }



        return binding.root
    }

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