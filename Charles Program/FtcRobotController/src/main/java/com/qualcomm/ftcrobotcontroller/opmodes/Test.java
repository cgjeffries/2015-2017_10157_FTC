package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by Charles II on 11/19/2015.
 */
public class Test {

    public void run()
    {
        AClass a = new AClass(9);
        a.setT(2);

        String s = new String("test");
        String s1 = "test";
    }


    class AClass
    {
        public int t = 0;

        public AClass(int i)
        {
            t = i;
        }

        public void setT(int i)
        {
            t = i;
        }
    }
}
