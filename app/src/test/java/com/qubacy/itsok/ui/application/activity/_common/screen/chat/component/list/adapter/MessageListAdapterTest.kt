package com.qubacy.itsok.ui.application.activity._common.screen.chat.component.list.adapter

import android.content.Context
import android.view.ViewGroup
import com.qubacy.itsok._common._test._common.util.mock.AnyMockUtil
import com.qubacy.itsok._common._test._common.util.mock.LayoutInflaterMockUtil
import com.qubacy.itsok.ui.application.activity._common.screen._common.data.message._test.util.UIMessageUtilGenerator
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.component.list.adapter.BaseRecyclerViewAdapterTest
import com.qubacy.itsok.ui.application.activity._common.screen.chat._common.data.message.UIMessage
import com.qubacy.itsok.ui.application.activity._common.screen.chat.component.message.view.active.ActiveMessageView
import com.qubacy.itsok.ui.application.activity._common.screen.chat.component.message.view.previous.PreviousMessageView
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import kotlin.reflect.KClass

class MessageListAdapterTest : BaseRecyclerViewAdapterTest<
    UIMessage, MessageListAdapter.MessageViewHolder, MessageListAdapter
>() {

    @Before
    override fun setup() {
        super.setup()
    }

    override fun createAdapter(): MessageListAdapter {
        return MessageListAdapter()
    }

    override fun getTestItems(count: Int): List<UIMessage> {
        return UIMessageUtilGenerator.generateUIMessages(count)
    }

    override fun spyAdapter(spiedAdapter: MessageListAdapter) {
        super.spyAdapter(spiedAdapter)

        Mockito.doAnswer{ }.`when`(spiedAdapter).scrollToActiveMessage()
    }

    @Test
    fun getItemViewTypeTest() {
        data class TestCase(
            val expectedItemViewTypeId: Int,
            val gottenItemViewTypeId: Int
        )

        val testCases = IntRange(0, 9).map {
            val expectedItemTypeId =
                if (it == 0) MessageListAdapter.ItemType.ACTIVE.id
                else MessageListAdapter.ItemType.PREVIOUS.id

            TestCase(expectedItemTypeId, mAdapter.getItemViewType(it))
        }

        for (testCase in testCases)
            Assert.assertEquals(testCase.expectedItemViewTypeId, testCase.gottenItemViewTypeId)
    }

    @Deprecated(
        "Not testable for now. It demands to refactor the whole View Holder " +
        "producing system (via Factory pattern, etc.)."
    )
    @Test
    fun onCreateViewHolderTest() {
        data class TestCase(
            val expectedViewHolderClass: KClass<out MessageListAdapter.MessageViewHolder>,
            val gottenViewHolderClass: KClass<out MessageListAdapter.MessageViewHolder>
        )

        val parentViewMock = Mockito.mock(ViewGroup::class.java)

        Mockito.`when`(parentViewMock.context).thenReturn(Mockito.mock(Context::class.java))

        val testCases = IntRange(0, 9).map {
            val viewTypeId = mAdapter.getItemViewType(it)
            val viewMock =
                if (it == 0) Mockito.mock(ActiveMessageView::class.java)
                else Mockito.mock(PreviousMessageView::class.java)

            LayoutInflaterMockUtil.getMockedLayoutInflater(viewMock)

            val expectedViewHolderClass =
                if (it == 0) MessageListAdapter.ActiveMessageViewHolder::class
                else MessageListAdapter.PreviousMessageViewHolder::class

            TestCase(
                expectedViewHolderClass,
                mAdapter.onCreateViewHolder(parentViewMock, viewTypeId)::class
            )
        }

        for (testCase in testCases)
            Assert.assertEquals(testCase.expectedViewHolderClass, testCase.gottenViewHolderClass)
    }

    @Test
    fun onBindViewHolderTest() {
        val messages = UIMessageUtilGenerator.generateUIMessages(2)

        setItemsToAdapter(messages)

        var setActiveMessage: UIMessage? = null
        var setPreviousMessage: UIMessage? = null

        val activeMessageViewHolderMock = Mockito.mock(
            MessageListAdapter.ActiveMessageViewHolder::class.java
        ).apply {
            Mockito.`when`(setData(
                AnyMockUtil.anyObject(),
                Mockito.anyBoolean(),
                AnyMockUtil.anyObject(),
                AnyMockUtil.anyObject())
            ).thenAnswer {
                val messageToSet = it.arguments[0] as UIMessage

                setActiveMessage = messageToSet

                Unit
            }
        }
        val previousMessageViewHolderMock = Mockito.mock(
            MessageListAdapter.PreviousMessageViewHolder::class.java
        ).apply {
            Mockito.`when`(setData(AnyMockUtil.anyObject()))
                .thenAnswer {
                    val messageToSet = it.arguments[0] as UIMessage

                    setPreviousMessage = messageToSet

                    Unit
                }
        }

        mAdapter.onBindViewHolder(activeMessageViewHolderMock, 0)
        mAdapter.onBindViewHolder(previousMessageViewHolderMock, 1)

        Assert.assertEquals(messages[0], setActiveMessage)
        Assert.assertEquals(messages[1], setPreviousMessage)
    }

    @Test
    fun addItemTest() {
        setItemsToAdapter(listOf())

        val expectedMessage = UIMessageUtilGenerator.generateUIMessage()
        val expectedMessageCount = 1

        mAdapter.addItem(expectedMessage)

        val gottenMessages = mAdapter.items

        Assert.assertEquals(expectedMessageCount, gottenMessages.size)

        val gottenMessage = gottenMessages.first()

        Assert.assertEquals(expectedMessage, gottenMessage)
    }

    @Test
    fun addItemsTest() {
        val initMessages = UIMessageUtilGenerator
            .generateUIMessages(2, "Init message")

        setItemsToAdapter(initMessages)

        val newMessages = UIMessageUtilGenerator.generateUIMessages(3)
        val expectedMessages = initMessages.apply { add(0, newMessages.first()) }

        mAdapter.addItems(newMessages)

        val gottenMessages = mAdapter.items

        Assert.assertEquals(expectedMessages.size, gottenMessages.size)

        for (i in expectedMessages.indices)
            Assert.assertEquals(expectedMessages[i], gottenMessages[i])
    }

    @Test
    fun setItemsTest() {
        val initMessages = UIMessageUtilGenerator
            .generateUIMessages(2, "Init message")

        setItemsToAdapter(initMessages)

        val newMessages = UIMessageUtilGenerator.generateUIMessages(2)
        val expectedMessages = newMessages.reversed()

        mAdapter.setItems(newMessages)

        val gottenMessages = mAdapter.items

        Assert.assertEquals(expectedMessages.size, gottenMessages.size)

        for (i in expectedMessages.indices)
            Assert.assertEquals(expectedMessages[i], gottenMessages[i])
    }
}