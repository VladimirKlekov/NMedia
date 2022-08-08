package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.adapter.PostEventListener
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


class FeedFragment : Fragment() {

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
//____________________________________________________________________________________________//


        val binding: FragmentFeedBinding = FragmentFeedBinding.inflate(
            inflater,
            container,
            false
        )

        val adapter = PostAdapter(
            object : PostEventListener {

                override fun onEdit(post: Post) {
                    viewModel.edit(post)
                    findNavController().navigate(
                        R.id.action_feedFragment_to_newPostFragment,
                        Bundle().apply {
                            textArg = post.content
                        }
                    )

                }

                override fun onRemove(post: Post) {
                    viewModel.removeById(post.id)
                }

                override fun onLike(post: Post) {
                    viewModel.likeById(post.id)
                }

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

                override fun onVideo(post: Post) {
                    val intentVideo = Intent(Intent.ACTION_VIEW, Uri.parse(post.video))
                    startActivity(intentVideo)
                }

                override fun onPost(post: Post) {
                    val action = FeedFragmentDirections.actionFeedFragmentToPostFragment(post.id.toInt())
                    findNavController().navigate(action)
                }

            }
        )
//____________________________________________________________________________________________//
        binding.container.adapter = adapter

//____________________________________________________________________________________________//
        viewModel.data.observe(viewLifecycleOwner)
        { posts ->
            //val newPost = adapter.itemCount < posts.size
            adapter.submitList(posts) {
               //if (newPost) binding.container.smoothScrollToPosition(0)
            }
        }
//____________________________________________________________________________________________//
        binding.addPost.setOnClickListener {
            //val args = NewPostFragmentArgs.Builder().setContent("111").build()
            val action = FeedFragmentDirections.actionFeedFragmentToNewPostFragment("111")
            findNavController().navigate(action)

//           findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
//            newPostContract.launch()
        }
        //____________________________________________________________________________________________//

//        parentFragmentManager.beginTransaction()
//            .replace(R.id.nav_main, PostFragment.newInstance("1", "2")).commit()

//____________________________________________________________________________________________//
        return binding.root
    }

    //вспомогательный объект для передачи данных  между фрагментами
    companion object {

        var Bundle.idArg: Int by IntArg
//        Выражение после by — делегат, потому что обращения (get(), set())
//        к свойству будут делегированы его методам getValue() и setValue()
    }

    object IntArg : ReadWriteProperty<Bundle, Int>
    //создаю обеъкт анонимного типа Чтение и Запись свойства для...?
    //с выозовом класса Bundle для временного хранения данных и числовым значением для..?
    {

        //функция для чтения какого-то значения из "временного хранения" и возврата числового знач.
        override fun getValue(thisRef: Bundle, property: KProperty<*>): Int {
            return thisRef.getInt(property.name)
        }

        //функция для записи какого-то значения из "временного хранения"
        override fun setValue(thisRef: Bundle, property: KProperty<*>, value: Int) {
            thisRef.putInt(property.name, value)
        }
    }
}