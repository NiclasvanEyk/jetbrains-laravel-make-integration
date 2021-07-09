package com.niclas_van_eyk.laravel_make_integration.extension.laravel.events.toolWindow

import com.intellij.ui.components.JBList

class EventList(commands: List<Any>) : JBList<Any>(commands) {
//    fun refreshEvents(newCommands: List<EventListenerPair>) {
//        (model as DefaultListModel).apply {
//            removeAllElements()
//            addAll(newCommands)
//        }
//    }

    init {
//        cellRenderer = object : ColoredListCellRenderer<EventListenerPair>() {
//            override fun customizeCellRenderer(
//                list: JList<out EventListenerPair>,
//                value: EventListenerPair?,
//                index: Int,
//                selected: Boolean,
//                hasFocus: Boolean
//            ) {
//                if (value != null) {
//                    append(value.event.name, SimpleTextAttributes.REGULAR_ATTRIBUTES, true)
//                    append(" ")
//                    append(value.listener.name, SimpleTextAttributes.REGULAR_ATTRIBUTES)
//                }
//            }
//        }

//        ListSpeedSearch(this) { "${it.event.name} ${it.listener.name}" }
    }
}
