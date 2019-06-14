package extension

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@UseExperimental(ExperimentalContracts::class)
inline fun Boolean?.yes(block: () -> Unit): Boolean? {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    if (this == true) block()
    return this
}

@UseExperimental(ExperimentalContracts::class)
inline fun Boolean?.no(block: () -> Unit): Boolean? {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    if (this != true) block()
    return this
}