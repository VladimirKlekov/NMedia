package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.FeedModel.FeedModelState
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
    //представляем ViewModel нескольким активити
    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentFeedBinding = FragmentFeedBinding.inflate(
            inflater,
            container,
            false
        )
        val adapter = PostAdapter(
            object : PostEventListener {
                /** --------------------------------------------------------------------------- **/
                override fun onEdit(post: Post) {
                    viewModel.edit(post)
                    findNavController().navigate(
                        R.id.action_feedFragment_to_newPostFragment,
                        Bundle().apply {
                            textArg = post.content

                        }
                    )
                }

                /** --------------------------------------------------------------------------- **/
                override fun onRemove(post: Post) {
                    viewModel.removeById(post.id)
                }

                /** --------------------------------------------------------------------------- **/
                override fun onLike(post: Post) {
                    if (post.likedByMe) {
                        viewModel.unlikeById(post.id)
                    } else {
                        viewModel.likeById(post.id)
                    }
                    viewModel.load()
                }

                /** --------------------------------------------------------------------------- **/
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

                /** --------------------------------------------------------------------------- **/
                override fun onVideo(post: Post) {
                    val intentVideo = Intent(Intent.ACTION_VIEW, Uri.parse(post.video))
                    startActivity(intentVideo)
                }

                /** --------------------------------------------------------------------------- **/
                override fun onPost(post: Post) {
                    val action =
                        FeedFragmentDirections.actionFeedFragmentToPostFragment(post.id.toInt())
                    findNavController().navigate(action)
                }
            }
        )
        /** ----------------------------------------------------------------------------------- **/
        binding.container.adapter = adapter
        /** ----------------------------------------------------------------------------------- **/
        viewModel.data.observe(viewLifecycleOwner) { state ->
            adapter.submitList(state.posts)
            binding.emptyText.isVisible = state.empty
        }
        viewModel.state.observe(viewLifecycleOwner) { state ->
            binding.progress.isVisible = state is FeedModelState
            binding.errorGroup.isVisible = state is FeedModelState
        }

        /** ----------------------------------------------------------------------------------- **/
        binding.retryButton.setOnClickListener {
            viewModel.load()
        }

        /** ---------------------------------------------------------------------------------- **/
        binding.addPost.setOnClickListener {
            val action = FeedFragmentDirections.actionFeedFragmentToNewPostFragment("111")
            findNavController().navigate(action)
        }

        /** -------добавляю для flow--------------------------------------------------------------- **/
        //подписываюсь на поток
        viewModel.newerCount.observe(viewLifecycleOwner) {
            //будем выводит информацию в консоль, сколько постов имеется
            if (it >= 1) {
                binding.notificationNewPostsButton.visibility = View.VISIBLE
                binding.count.text = it.toString()
            }
            //TODO ДЗ отражение сделать в домашней работе
            println("Newer count: $it")
        }

        binding.notificationNewPostsButton.setOnClickListener {
            binding.notificationNewPostsButton.visibility = View.GONE
            binding.notificationGroup.visibility = View.VISIBLE

        }
        binding.notificationNewerPostCountButton.setOnClickListener {
            binding.notificationGroup.visibility = View.GONE
            //TODO
        }

        /** -------------------------------------------------------------------------------------- **/
        return binding.root
    }

    /** -------------------------------------------------------------------------------------- **/
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

    object StringArgText : ReadWriteProperty<Bundle, String?> {
        override fun getValue(thisRef: Bundle, property: KProperty<*>): String? {
            return thisRef.getString(property.name)
        }

        override fun setValue(thisRef: Bundle, property: KProperty<*>, value: String?) {
            thisRef.putString(property.name, value)
        }
    }
}