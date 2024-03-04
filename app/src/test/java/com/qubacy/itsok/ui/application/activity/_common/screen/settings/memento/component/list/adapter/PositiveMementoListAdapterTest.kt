package com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.component.list.adapter

import com.qubacy.itsok._common._test.util.mock.AnyMockUtil
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.component.list.adapter.BaseRecyclerViewAdapterTest
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento._common.data.UIMemento
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento._common.data._test.util.UIMementoUtilGenerator
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.component.preview.PositiveMementoPreviewView
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.util.concurrent.atomic.AtomicLong

class PositiveMementoListAdapterTest(

) : BaseRecyclerViewAdapterTest<
    UIMemento,
    PositiveMementoListAdapter.PositiveMementoViewHolder,
    PositiveMementoListAdapter
>() {
    companion object {
        const val DEFAULT_UNSET_MEMENTO_ID_VALUE = -1L
    }

    private lateinit var mCallback: PositiveMementoListAdapterCallback

    private val mOnMementoRemovedResult: AtomicLong =
        AtomicLong(DEFAULT_UNSET_MEMENTO_ID_VALUE)

    @Before
    override fun setup() {
        super.setup()
    }

    override fun createAdapter(): PositiveMementoListAdapter {
        initCallback()

        return PositiveMementoListAdapter(mCallback)
    }

    @After
    fun clear() {
        resetData()
    }

    private fun initCallback() {
        mCallback = Mockito.mock(PositiveMementoListAdapterCallback::class.java)

        Mockito.`when`(mCallback.onMementoRemoved(Mockito.anyLong()))
            .thenAnswer {
                val mementoId = it.arguments[0] as Long

                mOnMementoRemovedResult.set(mementoId)
            }
    }

    private fun resetData() {
        mOnMementoRemovedResult.set(DEFAULT_UNSET_MEMENTO_ID_VALUE)
    }

    override fun getTestItems(count: Int): List<UIMemento> {
        return UIMementoUtilGenerator.generateUIMementoes(count)
    }

    @Test
    fun onBindViewHolderTest() {
        val mementoViewHolderMock = Mockito.mock(
            PositiveMementoListAdapter.PositiveMementoViewHolder::class.java)

        var setMemento: UIMemento? = null

        Mockito.`when`(mementoViewHolderMock.setData(AnyMockUtil.anyObject()))
            .thenAnswer {
                val memento = it.arguments[0] as UIMemento

                setMemento = memento

                Unit
            }
        Mockito.`when`(mementoViewHolderMock.view)
            .thenAnswer {
                Mockito.mock(PositiveMementoPreviewView::class.java)
            }

        val expectedMemento = UIMementoUtilGenerator.generateUIMemento()

        setItemsToAdapter(listOf(expectedMemento))

        mAdapter.onBindViewHolder(mementoViewHolderMock, 0)

        Assert.assertEquals(expectedMemento, setMemento)
    }

    @Test
    fun removeMementoAtTest() {
        val initMementoes = UIMementoUtilGenerator.generateUIMementoes(2)

        setItemsToAdapter(initMementoes)

        val mementoToRemoveIndex = 0
        val expectedMementoCount = initMementoes.size - 1
        val expectedRemovedMemento = initMementoes[mementoToRemoveIndex]

        mAdapter.removeMementoAt(mementoToRemoveIndex)

        assertMementoRemoval(expectedMementoCount, expectedRemovedMemento)
    }

    @Test
    fun onMementoSwipedOutTest() {
        val initMementoes = UIMementoUtilGenerator.generateUIMementoes(2)

        setItemsToAdapter(initMementoes)

        val mementoToRemoveIndex = 0
        val expectedMementoCount = initMementoes.size - 1
        val expectedRemovedMemento = initMementoes[mementoToRemoveIndex]

        mAdapter.onMementoSwipedOut(mementoToRemoveIndex)

        assertMementoRemoval(expectedMementoCount, expectedRemovedMemento)
    }

    private fun assertMementoRemoval(expectedMementoCount: Int, expectedRemovedMemento: UIMemento) {
        Assert.assertEquals(expectedMementoCount, mAdapter.items.size)
        Assert.assertFalse(mAdapter.items.contains(expectedRemovedMemento))
        Assert.assertEquals(expectedRemovedMemento.id, mOnMementoRemovedResult.get())
    }

    @Test
    fun addMementoTest() {
        val initMementoes = UIMementoUtilGenerator.generateUIMementoes(2)

        setItemsToAdapter(initMementoes)

        val newMemento = UIMementoUtilGenerator.generateUIMemento(text = "new memento")
        val expectedMementoes = initMementoes.plus(newMemento)

        mAdapter.addMemento(newMemento)

        Assert.assertEquals(expectedMementoes.size, mAdapter.items.size)

        for (i in expectedMementoes.indices)
            Assert.assertEquals(expectedMementoes[i], mAdapter.items[i])
    }

    @Test
    fun updateMementoTest() {
        val initMementoes = UIMementoUtilGenerator.generateUIMementoes(2)

        setItemsToAdapter(initMementoes)

        val updatedMementoIndex = 0
        val updatedMemento = initMementoes[updatedMementoIndex].copy(text = "updated memento")
        val expectedMementoes = initMementoes.toMutableList()
            .apply { this[updatedMementoIndex] = updatedMemento }

        mAdapter.updateMemento(updatedMemento, updatedMementoIndex)

        Assert.assertEquals(expectedMementoes.size, mAdapter.items.size)

        for (i in expectedMementoes.indices)
            Assert.assertEquals(expectedMementoes[i], mAdapter.items[i])
    }

    @Test
    fun setMementoesTest() {
        val initMementoes = UIMementoUtilGenerator.generateUIMementoes(2)

        setItemsToAdapter(initMementoes)

        val expectedMementoes = UIMementoUtilGenerator
            .generateUIMementoes(3, "new memento")

        mAdapter.setMementoes(expectedMementoes)

        Assert.assertEquals(expectedMementoes.size, mAdapter.items.size)

        for (i in expectedMementoes.indices)
            Assert.assertEquals(expectedMementoes[i], mAdapter.items[i])
    }
}