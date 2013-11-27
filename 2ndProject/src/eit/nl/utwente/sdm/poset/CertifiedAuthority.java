package eit.nl.utwente.sdm.poset;

import java.util.Random;

public class CertifiedAuthority {
	
	public CertifiedAuthority(){
	}
	
	public int generatePrime(){
		int max = 100000;	 
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
            System.out.print(i + " ");
            prime = i;
        	}
        }
        return prime;
	}
	
	//
	public void keyAssignment(Poset p){
		Integer prime;
		Integer g;
		
		prime = generatePrime();
		g = new Integer(0);
		Random rn = new Random();
		g = rn.nextInt(prime);

		//VirtualNodes and ConsultNode don't have parents! they are MAX(P) p.173 book
		for (NodeX x : p.nodeList){
			if((x.getFlag()==1) || (x.getFlag()==0)){
				
			}
		}
	}
}
