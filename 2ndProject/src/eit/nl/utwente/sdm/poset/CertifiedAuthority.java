package eit.nl.utwente.sdm.poset;

import java.math.BigInteger;
import java.util.Random;

public class CertifiedAuthority {
	public static BigInteger prime;
	public static BigInteger g;
	public CertifiedAuthority(){
	}
	
	public int generatePrime(){
		int max = 1000;	 
		int prime = 0;
		System.out.println("Generate Prime numbers between 1 and " + max);
		// loop through the numbers one by one
        for (int i = 1; i<max; i++) {
    	boolean isPrimeNumber = true;
		
		// check to see if the number is prime
	    for (int j = 2; j < i; j++) {
	    	if (i % j == 0) {
	            isPrimeNumber = false;
	            break; // exit the inner for loop
	        }
	    }
			             
        // print the number if prime
        if (isPrimeNumber) {
            prime = i;
        	}
        }
        return prime;
	}
	
	public static BigInteger rndBigInt(BigInteger max) {
	    Random rnd = new Random();
	    do {
	        BigInteger i = new BigInteger(max.bitLength(), rnd);
	        if (i.compareTo(max) <= 0)
	            return i;
	    } while (true);
	}
	
	public BigInteger keyAssignment(Poset p){
		BigInteger one;
		BigInteger privateKeyMax;
		BigInteger publicKeyMax;
		
		prime = new BigInteger (generatePrime()+"");
		System.out.println("prime = " + prime);
		g = new BigInteger(0+"");
		one = new BigInteger(1+"");
		g = rndBigInt(prime.subtract(one));

		//VirtualNodes and ConsultNode don't have parents! they are MAX(P) p.173 book
		for (NodeX x : p.nodeList){
			if((x.getFlag()==1) || (x.getFlag()==0)){
				privateKeyMax = rndBigInt(prime);
				x.setPrivateKey(privateKeyMax);
				publicKeyMax = g.pow(privateKeyMax.intValue());
				publicKeyMax = publicKeyMax.mod(prime);
				x.setPublicKey(publicKeyMax);
			}
		}
		for (NodeX x : p.nodeList){
			if (x.getFlag()==2){
				BigInteger secretVirtual = x.getParentVirtual().getPrivateKey();
				BigInteger secretCons = x.getParentConsul().getPrivateKey();
				BigInteger exponent = secretCons.multiply(secretVirtual);
				BigInteger secretKeyChild = g.pow(Integer.parseInt(exponent.toString()));
				secretKeyChild = secretKeyChild.mod(prime);
				BigInteger publicKeyChild = g.pow(secretKeyChild.intValue());
				publicKeyChild = publicKeyChild.mod(prime);
				x.setPrivateKey(secretKeyChild);
				x.setPublicKey(publicKeyChild);
			}
		}
		return g;
	}
}
