package Main;

import model.pojo.business.other.PersonnePhysique;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class MainTest{
    @Test
    public void getLoginStage() throws IllegalAccessException{
        PersonnePhysique ph = new PersonnePhysique();
        ph.setNom("RATOMBOTANA Armand Judicael");
        ph.setAdresse("Ambalamanasy carreau 1");
        ph.setLot("60");
        ph.setMere("VOANGY");
        ph.setPere("TOMBO");
        ph.setLieuDelivranceCin("Tamatave");
        ph.setNumCin("3A3A36666663T63");
        Class<? extends PersonnePhysique> phClass = ph.getClass();
        Field[] declaredFields = phClass.getDeclaredFields();
        System.out.println(declaredFields.length);
        for (Field declaredField : declaredFields) {
            Class<?> type = declaredField.getType();
            if (type == String.class){
                declaredField.setAccessible(true);
                String name = declaredField.getName();
                String value = (String)declaredField.get(ph);
                System.out.println(" name = "+name+" ,value = "+value);
            }
        }
    }
    private enum testEnum {
        TEST_ENUM, YUM , DNF
    }
    @Test public void serverIsReacheable(){
            testEnum onum = MainTest.testEnum.DNF;
        System.out.println(testEnum.class.getSuperclass().equals(java.lang.Enum.class));
//        Boolean aBoolean = Main.serverIsReacheable("127.0.0.1");
//        Boolean aBoolean1 = Main.serverIsReacheable("192.168.1.50");
//        assertTrue(aBoolean1);
//        assertTrue(aBoolean);
    }
}