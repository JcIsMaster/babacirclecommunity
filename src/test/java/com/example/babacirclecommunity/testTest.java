package com.example.babacirclecommunity;

import com.example.babacirclecommunity.common.constanct.PointsType;
import com.example.babacirclecommunity.common.utils.HonoredPointsUtil;
import com.example.babacirclecommunity.common.utils.TimeUtil;
import com.example.babacirclecommunity.honored.entity.HonoredPoints;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class testTest {

    @BeforeEach
    void setUp() {
//        System.out.println("before");
    }

    @AfterEach
    void tearDown() {
//        System.out.println("after");
    }

    @Test
    void main() {
        /**
         * ==&equals
        String x = "通话";
        String y = "重地";
        System. out. println(String. format("x：%d | y：%d", x. hashCode(),y. hashCode()));
        String z = new String("string");
        System.out.println(x==y); // true
        System.out.println(x==z); // false
        System.out.println(x.equals(y)); // true
        System.out.println(x.equals(z)); // true
         */

        /**
         * Math.round
        System.out.println(Math.round(1.6));
        System.out.println(Math.round(-1.6));
        System.out.println(Math.round(1.5));
        System.out.println(Math.round(-1.5));
        System.out.println(Math.round(1.4));
        System.out.println(Math.round(-1.4));
         */

        /**
        String a = " a bcdefg ";
        System.out.println(a.trim());
         */

//        System.out.println(oddSum(10)); //54

        /**
        Queue queue = new LinkedList();
        queue. offer("string"); // add
        System. out. println(queue. poll()); // 无元素null
        System. out. println(queue. remove()); //无元素NoSuchElementException
        System. out. println(queue. size());
         */

        /**
        List list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        Iterator it = list. iterator();
        while(it. hasNext()){
            String obj = String.valueOf(it.next());
            System.out.println(obj);
        }
         */

        /**
        for (int i : removeDuplicates(new int[]{1,1,2,2,2,2,3,4,4,5,5,5,6})) {
            System.out.println(i);
        }
        System.out.println(Arrays.toString(removeDuplicates(new int[]{1,1,2,2,2,2,3,4,4,5,5,5,6})));
        */

        /**
         * System.out.println(maxProfit(new int[]{7,1,5,3,6,4}));
         */

//        int nums[] = {1,3,1,-1,3};//-1,1,1,3,3
//        int reduce = 0;
//        for (int num : nums) {
//            reduce = reduce ^ num;
//            System.out.println(reduce);
//        }
//        System.out.println(1^3);
//        System.out.println(TimeUtil.getThisTime(Long.parseLong("1627542284")));

//        try {
//            String time = "2021-09-21";
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd");
//            System.out.println(TimeUtil.getEndOfDay(simpleDateFormat.parse(time)));
//        }
//        catch (Exception e){
//            System.out.println(e);
//        }
//        for (int i = 0; i < 50; i++) {
////            BigDecimal a = BigDecimal.valueOf(Math.random() * 10 + 0.1).setScale(1,BigDecimal.ROUND_DOWN);
//
////            System.out.println(a.setScale(2,BigDecimal.ROUND_DOWN));
//            System.out.println(BigDecimal.valueOf(Math.random() * 5 + 0.1).setScale(1,BigDecimal.ROUND_DOWN));
//        }
//        HonoredPointsUtil.addHonoredPoints(416, PointsType.HONORED_POINTS_MATCH,0,String.valueOf(System.currentTimeMillis()/1000));
        int a = 100;
        String b = "" + (int)(a * 0.98);
        System.out.println(b);
    }

    int oddSum(int n){
        if(n == 1){
            return 1;
        }
        if(n % 2 == 1){
            return n + evenSum(n - 1); //9+7+5+3+2=26
        }
        return evenSum(n);
    }

    int evenSum(int n){
        if (n == 2){
            return 2;
        }
        if (n % 2 == 0){
            return n + oddSum(n - 1); //10+8+6+4=28
        }
        return oddSum(n);
    }

    abstract class cas{
        public abstract int getInt(int a);
    }

    class bas extends cas{

        @Override
        public int getInt(int a) {
            return a;
        }
    }

    //删除排序数组中的重复项(双指针)
    public int[] removeDuplicates(int[] nums) {
        //边界条件判断
        if (nums == null || nums.length == 0)
            return null;
        int left = 0;
        for (int right = 1; right < nums.length; right++)
            //如果左指针和右指针指向的值一样，说明有重复的，
            //这个时候，左指针不动，右指针继续往右移。如果他俩
            //指向的值不一样就把右指针指向的值往前挪
            if (nums[left] != nums[right])
                nums[++left] = nums[right];
        return java.util.Arrays.copyOf(nums,++left);
    }

    //数组中低买高卖计算最高收益
    public int maxProfit(int[] prices) {
        int max = 0;
        for (int i = 1; i < prices.length; i++) {
            if (prices[i] > prices[i - 1]) {
                max = max + (prices[i] - prices[i - 1]);
            }
        }
        return max;
    }

    //旋转数组(关键点在于(i+k) % length,右偏移多少和数组长度求余)(反转解法reverse)
    public void rotate(int nums[], int k) {
        int length = nums.length;
        int temp[] = new int[length];
        //把原数组值放到一个临时数组中，
        for (int i = 0; i < length; i++) {
            temp[i] = nums[i];
        }
        //然后在把临时数组的值重新放到原数组，并且往右移动k位
        for (int i = 0; i < length; i++) {
            nums[(i + k) % length] = temp[i];
        }
    }


}