package dataclasses

// branch is S_i and T_i

data class Branch(
    val formings: MutableList<Permutation>,
    val tree: Tree
)