package com.tokopedia.oilreservoir

object Solution {
    fun collectOil(height: IntArray): Int {
        // TODO, return the amount of oil blocks that could be collected
        // below is stub
        var left = 0
        var right = height.size - 1
        var res = 0
        var maxleft = 0
        var maxright = 0
        while (left <= right) {
            if (height[left] <= height[right]) {
                if (height[left] >= maxleft) {
                    maxleft = height[left]
                } else {
                    res += maxleft - height[left]
                }
                left++
            } else {
                if (height[right] >= maxright) {
                    maxright = height[right]
                } else {
                    res += maxright - height[right]
                }
                right--
            }
        }
        return res
    }
}
