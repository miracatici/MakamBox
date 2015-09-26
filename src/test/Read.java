package test;

import java.io.File;

import applied.SelectCulture;

public class Read {
	public static void main(String[] args){
		File af = new File(SelectCulture.class.getResource("/TurkishCulture.ser").getPath());
		System.out.println(SelectCulture.class.getResource("/TurkishCulture.ser").toString());
		System.out.println(af.isFile());
	}	
}
