package com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.component.list.adapter

import androidx.recyclerview.widget.RecyclerView
import com.qubacy.itsok.ui.application.activity._common.screen._common.data.message._test.util.UIMessageUtilGenerator
import com.qubacy.itsok.ui.application.activity._common.screen.chat._common.data.message.UIMessage
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

abstract class BaseRecyclerViewAdapterTest<
    ItemType,
    ViewHolderType : RecyclerView.ViewHolder,
    AdapterType : BaseRecyclerViewAdapter<ItemType, ViewHolderType>
>(
    private val mAdapterClass: Class<AdapterType>
) {
    protected lateinit var mAdapter: AdapterType

    @Before
    open fun setup() {
        init()
    }

    private fun init() {
        initAdapter()
    }

    private fun initAdapter() {
        val spiedAdapter = Mockito.spy(mAdapterClass)

        spyAdapter(spiedAdapter)

        mAdapter = spiedAdapter
    }

    protected open fun spyAdapter(spiedAdapter: AdapterType) {
        Mockito.doAnswer{ }.`when`(spiedAdapter).wrappedNotifyDataSetChanged()
        Mockito.doAnswer{ }.`when`(spiedAdapter).wrappedNotifyItemInserted(Mockito.anyInt())
        Mockito.doAnswer{ }.`when`(spiedAdapter)
            .wrappedNotifyItemRangeInserted(Mockito.anyInt(), Mockito.anyInt())
        Mockito.doAnswer{ }.`when`(spiedAdapter).wrappedNotifyItemChanged(Mockito.anyInt())
        Mockito.doAnswer{ }.`when`(spiedAdapter)
            .wrappedNotifyItemRangeChanged(Mockito.anyInt(), Mockito.anyInt())
        Mockito.doAnswer{ }.`when`(spiedAdapter)
            .wrappedNotifyItemMoved(Mockito.anyInt(), Mockito.anyInt())
        Mockito.doAnswer{ }.`when`(spiedAdapter).wrappedNotifyItemRemoved(Mockito.anyInt())
        Mockito.doAnswer{ }.`when`(spiedAdapter)
            .wrappedNotifyItemRangeRemoved(Mockito.anyInt(), Mockito.anyInt())
    }

    @Test
    fun getItemCountTest() {
        Assert.assertEquals(0, mAdapter.itemCount)

        val messages = UIMessageUtilGenerator.generateUIMessages(3)

        setMessagesToAdapter(messages)

        Assert.assertEquals(messages.size, mAdapter.itemCount)
    }

    @Test
    fun resetItemsTest() {
        val initMessages = UIMessageUtilGenerator
            .generateUIMessages(2, "Init message")

        setMessagesToAdapter(initMessages)

        mAdapter.resetItems()

        Assert.assertTrue(mAdapter.items.isEmpty())
    }

    protected fun setMessagesToAdapter(messages: List<UIMessage>) {
        BaseRecyclerViewAdapter::class.java.getDeclaredField("mItems")
            .apply {
                isAccessible = true

                set(mAdapter, messages.toMutableList())
            }
    }
}