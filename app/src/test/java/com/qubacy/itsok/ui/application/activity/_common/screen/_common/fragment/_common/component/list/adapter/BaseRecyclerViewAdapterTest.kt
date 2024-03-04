package com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.component.list.adapter

import androidx.recyclerview.widget.RecyclerView
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

abstract class BaseRecyclerViewAdapterTest<
    ItemType,
    ViewHolderType : RecyclerView.ViewHolder,
    AdapterType : BaseRecyclerViewAdapter<ItemType, ViewHolderType>
> {
    protected lateinit var mAdapter: AdapterType

    @Before
    open fun setup() {
        init()
    }

    private fun init() {
        initAdapter()
    }

    private fun initAdapter() {
        val adapter = createAdapter()
        val spiedAdapter = Mockito.spy(adapter)

        spyAdapter(spiedAdapter)

        mAdapter = spiedAdapter
    }

    protected abstract fun createAdapter(): AdapterType

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

        val messages = getTestItems(3)

        setItemsToAdapter(messages)

        Assert.assertEquals(messages.size, mAdapter.itemCount)
    }

    @Test
    fun resetItemsTest() {
        val initMessages = getTestItems(2)

        setItemsToAdapter(initMessages)

        mAdapter.resetItems()

        Assert.assertTrue(mAdapter.items.isEmpty())
    }

    protected abstract fun getTestItems(count: Int): List<ItemType>

    protected fun setItemsToAdapter(items: List<ItemType>) {
        BaseRecyclerViewAdapter::class.java.getDeclaredField("mItems")
            .apply {
                isAccessible = true

                set(mAdapter, items.toMutableList())
            }
    }
}