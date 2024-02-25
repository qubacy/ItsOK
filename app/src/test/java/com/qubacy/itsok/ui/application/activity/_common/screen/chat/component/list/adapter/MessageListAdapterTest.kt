package com.qubacy.itsok.ui.application.activity._common.screen.chat.component.list.adapter

import android.view.ViewGroup
import com.qubacy.itsok._common._test.util.mock.AnyMockUtil
import com.qubacy.itsok._common._test.util.mock.LayoutInflaterMockUtil
import com.qubacy.itsok.ui.application.activity._common.screen._common.data.message._test.util.UIMessageUtilGenerator
import com.qubacy.itsok.ui.application.activity._common.screen.chat._common.data.message.UIMessage
import com.qubacy.itsok.ui.application.activity._common.screen.chat.component.message.view.active.ActiveMessageView
import com.qubacy.itsok.ui.application.activity._common.screen.chat.component.message.view.previous.PreviousMessageView
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import kotlin.reflect.KClass

class MessageListAdapterTest {
    private lateinit var mMessageListAdapter: MessageListAdapter

    @Before
    fun setup() {
        mMessageListAdapter = MessageListAdapter()
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

            TestCase(expectedItemTypeId, mMessageListAdapter.getItemViewType(it))
        }

        for (testCase in testCases)
            Assert.assertEquals(testCase.expectedItemViewTypeId, testCase.gottenItemViewTypeId)
    }

    @Test
    fun onCreateViewHolderTest() {
        data class TestCase(
            val expectedViewHolderClass: KClass<out MessageListAdapter.MessageViewHolder>,
            val gottenViewHolderClass: KClass<out MessageListAdapter.MessageViewHolder>
        )

        val parentViewMock = Mockito.mock(ViewGroup::class.java)
        val testCases = IntRange(0, 9).map {
            val viewTypeId = mMessageListAdapter.getItemViewType(it)
            val viewMock =
                if (it == 0) Mockito.mock(ActiveMessageView::class.java)
                else Mockito.mock(PreviousMessageView::class.java)

            LayoutInflaterMockUtil.getMockedLayoutInflater(viewMock)

            val expectedViewHolderClass =
                if (it == 0) MessageListAdapter.ActiveMessageViewHolder::class
                else MessageListAdapter.PreviousMessageViewHolder::class

            TestCase(
                expectedViewHolderClass,
                mMessageListAdapter.onCreateViewHolder(parentViewMock, viewTypeId)::class
            )
        }

        for (testCase in testCases)
            Assert.assertEquals(testCase.expectedViewHolderClass, testCase.gottenViewHolderClass)
    }

    @Test
    fun onBindViewHolderTest() {
        val messages = UIMessageUtilGenerator.generateUIMessages(2)

        setMessagesToAdapter(messages)

        var setActiveMessage: UIMessage? = null
        var setPreviousMessage: UIMessage? = null

        val activeMessageViewHolderMock = Mockito.mock(MessageListAdapter.ActiveMessageViewHolder::class.java).apply {
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
        val previousMessageViewHolderMock = Mockito.mock(MessageListAdapter.PreviousMessageViewHolder::class.java).apply {
            Mockito.`when`(setData(AnyMockUtil.anyObject()))
                .thenAnswer {
                    val messageToSet = it.arguments[0] as UIMessage

                    setPreviousMessage = messageToSet

                    Unit
                }
        }

        mMessageListAdapter.onBindViewHolder(activeMessageViewHolderMock, 0)
        mMessageListAdapter.onBindViewHolder(previousMessageViewHolderMock, 1)

        Assert.assertEquals(messages[0], setActiveMessage)
        Assert.assertEquals(messages[1], setPreviousMessage)
    }

    @Test
    fun getItemCountTest() {
        Assert.assertEquals(0, mMessageListAdapter.itemCount)

        val messages = UIMessageUtilGenerator.generateUIMessages(3)

        setMessagesToAdapter(messages)

        Assert.assertEquals(messages.size, mMessageListAdapter.itemCount)
    }

    private fun setMessagesToAdapter(messages: List<UIMessage>) {
        MessageListAdapter::class.java.getDeclaredField("mItems")
            .apply {
                isAccessible = true

                set(mMessageListAdapter, messages)
            }
    }
}