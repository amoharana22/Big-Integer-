package bigint;


/**
 * This class encapsulates a BigInteger, i.e. a positive or negative integer with 
 * any number of digits, which overcomes the computer storage length limitation of 
 * an integer.
 * 
 */
public class BigInteger {

	/**
	 * True if this is a negative integer
	 */
	boolean negative;

	/**
	 * Number of digits in this integer
	 */
	int numDigits;

	/**
	 * Reference to the first node of this integer's linked list representation
	 * NOTE: The linked list stores the Least Significant Digit in the FIRST node.
	 * For instance, the integer 235 would be stored as:
	 *    5 --> 3  --> 2
	 *    
	 * Insignificant digits are not stored. So the integer 00235 will be stored as:
	 *    5 --> 3 --> 2  (No zeros after the last 2)        
	 */
	DigitNode front;

	/**
	 * Initializes this integer to a positive number with zero digits, in other
	 * words this is the 0 (zero) valued integer.
	 */
	public BigInteger() {
		negative = false;
		numDigits = 0;
		front = null;
	}

	/**
	 * Parses an input integer string into a corresponding BigInteger instance.
	 * A correctly formatted integer would have an optional sign as the first 
	 * character (no sign means positive), and at least one digit character
	 * (including zero). 
	 * Examples of correct format, with corresponding values
	 *      Format     Value
	 *       +0            0
	 *       -0            0
	 *       +123        123
	 *       1023       1023
	 *       0012         12  
	 *       0             0
	 *       -123       -123
	 *       -001         -1
	 *       +000          0
	 *       
	 * Leading and trailing spaces are ignored. So "  +123  " will still parse 
	 * correctly, as +123, after ignoring leading and trailing spaces in the input
	 * string.
	 * 
	 * Spaces between digits are not ignored. So "12  345" will not parse as
	 * an integer - the input is incorrectly formatted.
	 * 
	 * An integer with value 0 will correspond to a null (empty) list - see the BigInteger
	 * constructor
	 * 
	 * @param integer Integer string that is to be parsed
	 * @return BigInteger instance that stores the input integer.
	 * @throws IllegalArgumentException If input is incorrectly formatted
	 */
	public static BigInteger parse(String integer) 
			throws IllegalArgumentException {

		if(integer.isEmpty()==true) {
			throw new IllegalArgumentException();
		}


		int index=0;
		BigInteger ans = new BigInteger();
		char error=integer.charAt(index);

		if(Character.isAlphabetic(error)==true) { 
			throw new IllegalArgumentException();
		}

		if(integer.charAt(index)=='-') {      
			ans.negative=true;
			index++;
		}else if(integer.charAt(index)=='+') { 
			ans.negative=false;
			index++;
		}

		for(int i=index; i<integer.length(); i++) {

			if(Character.isDigit(integer.charAt(i))==false) {   
				throw new IllegalArgumentException("Not Valid Input"); 

			}
			else if(integer.charAt(i) != '0') {

				break;
			}else if(index==integer.length()-1) {   
				return ans;
			}


			index++;
		}

		int counter=1;
		char first=integer.charAt(index);

		int firstnumber=Character.getNumericValue(first);

		DigitNode frontll=new DigitNode(firstnumber,null);
		index++;
		DigitNode dignode = null;
		for(int x=index; x<integer.length(); x++) {

			char ch=integer.charAt(x);

			if(Character.isAlphabetic(ch)==true || integer.charAt(x)==' ') {  
				throw new IllegalArgumentException("Not Valid Input");              
			}else if(Character.isDigit(ch)==false) {
				throw new IllegalArgumentException();
			}

			if(Character.isDigit(ch)==true) {

				int digit=Character.getNumericValue(ch);


				dignode=new DigitNode(digit, frontll);

				frontll=dignode;

				counter++;
			}

		}



		ans.numDigits=counter;
		ans.front=frontll;




		return ans;
	}

	/**
	 * Adds the first and second big integers, and returns the result in a NEW BigInteger object. 
	 * DOES NOT MODIFY the input big integers.
	 * 
	 * NOTE that either or both of the input big integers could be negative.
	 * (Which means this method can effectively subtract as well.)
	 * 
	 * @param first First big integer
	 * @param second Second big integer
	 * @return Result big integer
	 */
	public static BigInteger add(BigInteger first, BigInteger second) { 
		BigInteger ans=new BigInteger();

		if(first.negative==false && second.negative==false) {


			ans=addhelper(first,second);

			ans.negative=false;

		}

		if(first.negative==true && second.negative==true) {


			ans=addhelper(first,second);

			ans.negative=true;
		}









		if((first.negative==true || second.negative==true) && (first.numDigits>second.numDigits) && first.negative!=second.negative) {
			ans=addsubtractor(first,second);

			ans.negative=first.negative;

		}

		if((first.negative==true || second.negative==true) &&(first.numDigits<second.numDigits) && first.negative!=second.negative) {
			ans=addsubtractor(second,first);

			ans.negative=second.negative;
		}

		if((first.negative==true || second.negative==true) && (first.numDigits==second.numDigits) && first.negative!=second.negative){
			if(onebigger(first,second)==true) {

				ans=addsubtractor(first,second);   


				ans.negative=first.negative;

			}
			else if(onebigger(first,second)==false) {
				ans=addsubtractor(second,first);

				ans.negative=second.negative;
			}
		}


		


		String tempor=ans.toString();

		ans=parse(tempor);


		return ans;

	}	

	private static BigInteger addsubtractor(BigInteger first, BigInteger second) {
		BigInteger ans=new BigInteger();

		DigitNode one=first.front;

		DigitNode two=second.front;








		if((first.numDigits<second.numDigits) || (first.numDigits==second.numDigits && onebigger(first,second)==false)) {

			DigitNode temp=one;

			one=two;

			two=temp;


		}

		DigitNode answer=null;

		DigitNode pointer=null;

		DigitNode digit=null;
		int diff=0;

		boolean borrow=false;
		DigitNode bottom=second.front;

		DigitNode top=first.front;

	

		int topnum=0;

		int bottnum=0;

		while(top!=null || bottom!=null) {
			if(top!=null) {
				topnum=top.digit; 
			}else if(top==null) {
				topnum=0;
			}


			if(bottom!=null) {
				bottnum=bottom.digit;
			}else if(bottom==null) {
				bottnum=0;
			}

			
			if(borrow) {

				if(topnum!=0 ) {
					topnum=topnum-1;  
					borrow=false;
				}else if(topnum==0 ) {
					topnum=topnum+9;
				}


			}
			if(top!=null && bottom!=null && topnum<bottnum) {
				topnum=topnum+10; //

				borrow=true;
			}


			if(top!=null && top!=null ) {  
				diff=topnum-bottnum;
			}else if(first.front!=null &&second.front==null ) {
				diff=topnum;
			}


			


			digit=new DigitNode(diff,null);






			if(answer==null) {
				answer=digit; 
			}else if(answer!=null) {
				pointer.next=digit;
			}

			pointer=digit;

			if(top!=null) {
				top=top.next;
			}
			if(bottom!=null) {
				bottom=bottom.next;
			}

		}








		ans.front=answer;



		int counter=0;



		while(answer!=null) {

			counter++;

			answer=answer.next;


		}

		ans.numDigits=counter;



		return ans;
	}





	private static boolean onebigger(BigInteger first, BigInteger second) {

		boolean onebig=true;

	
		DigitNode one=first.front;

		DigitNode two=second.front;

	 

		DigitNode onetrav=first.front;

		DigitNode twotrav=second.front;

		while(onetrav!=null || twotrav!=null) {

			if(onetrav.digit>twotrav.digit) {
				onebig=true;
			}else if(onetrav.digit<twotrav.digit){
				onebig=false;
			}

			if(onetrav!=null) {
				onetrav=onetrav.next;
			}
			if(twotrav!=null) {
				twotrav=twotrav.next;
			}


		}

		onetrav=one;

		twotrav=two;  


		return onebig;
	}


	private static BigInteger addhelper(BigInteger first, BigInteger second) {
		BigInteger ans=new BigInteger();

		int sum=0;

		int carry=0;

		DigitNode answer=null;

		DigitNode pointer=null;

		DigitNode digit=null;


	

		DigitNode onetrav=first.front;

		DigitNode twotrav=second.front;

		while(onetrav!=null || twotrav!=null) {
			if(onetrav!=null && twotrav!=null) {
			

				sum=carry+(onetrav.digit)+(twotrav.digit); 


				if(sum>=10) {
					sum=sum%10;
					carry=1;
				}else if(sum<10 && sum>-10) {
					carry=0;
				}

			}else if(onetrav!=null && twotrav==null) {

				sum=carry+0+onetrav.digit;;



				if(sum>=10) {
					sum=sum%10;
					carry=1;
				}else if(sum<10 && sum>-10) {
					carry=0;
				}


			}else if(onetrav==null && twotrav!=null) {     

				sum=carry+twotrav.digit+0;



				if(sum>=10) {
					sum=sum%10;
					carry=1;
				}else if(sum<10 && sum>-10) {
					carry=0;
				}

			}

			digit=new DigitNode(sum,null);


			if(answer==null) {
				answer=digit; 
			}else if(answer!=null) {
				pointer.next=digit;
			}

			pointer=digit;

			if(onetrav!=null) {
				onetrav=onetrav.next;
			}
			if(twotrav!=null) {
				twotrav=twotrav.next;
			}


			if(carry>0) {
				pointer.next=new DigitNode(carry,null);

			}



		} 
		ans.front=answer;

		int counter=0;

		while(answer!=null) {
			counter++;

			answer=answer.next;


		} 





		ans.numDigits=counter;
		


		return ans;

	}








	/**
	 * Returns the BigInteger obtained by multiplying the first big integer
	 * with the second big integer
	 * 
	 * This method DOES NOT MODIFY either of the input big integers
	 * 
	 * @param first First big integer
	 * @param second Second big integer
	 * @return A new BigInteger which is the product of the first and second big integers
	 */
	public static BigInteger multiply(BigInteger first, BigInteger second) {



		BigInteger ans=new BigInteger();

		if(first.front==null || second.front==null) {
			return ans;
		}



		BigInteger adding=new BigInteger();  


		int product=0;
		int remainder=0;


		DigitNode answer=null;

		DigitNode pointer=null;

		DigitNode firstll=null;

		

		DigitNode top=first.front;

		DigitNode firstholder=top;    

		DigitNode bottom=second.front;

		int topnum=0;

		int botnum=0;

		while(top!=null) {

			topnum=top.digit;


			if(bottom!=null) {
				botnum=bottom.digit; 
			}

			product=remainder+(topnum*botnum);

			if(product>=10) {
				remainder=product / 10;

				product=product%10;

				// check remainder status
			}else if(product<=9) {
				remainder=0;
			}

			firstll=new DigitNode(product,null);


			if(answer==null) {
				answer=firstll; 
			}else if(answer!=null) {
				pointer.next=firstll;
			}

			pointer=firstll;

			if(top!=null) {
				top=top.next;

			}

			if(remainder>0) {
				pointer.next=new DigitNode(remainder,null);

			}


		}


		ans.front=answer; 

		DigitNode bot2=second.front.next;

		DigitNode answer2=null;

		DigitNode pointer2=null;

		DigitNode newll=null;

		int zerolimt=0;

		int product2=0;

		int remainder2=0;

		while(bot2!=null) {
			botnum=bot2.digit;
			zerolimt++;
			top=firstholder;
			while (top!=null) {
				topnum=top.digit;
				product2=remainder2+(topnum*botnum);

				if(product2<=9) {
					remainder2=0;
				}else if(product2>=10) {


					remainder2=product2/10;

					product2=product2%10;
				}


				newll=new DigitNode(product2,null);

				if(answer2==null) {
					answer2=newll; 
				}else if(answer2!=null) {
					pointer2.next=newll;
				}

				pointer2=newll;


				if(top!=null) {
					top=top.next;
				}

				if(remainder2>0) {  
					pointer2.next=new DigitNode(remainder2,null);

				}

			}

			int i=1;


			do { 
				DigitNode zero=new DigitNode(0,answer2); 

				answer2=zero;

				i++;

			}while(i<=zerolimt);


			adding.front=answer2;

			ans=add(ans,adding);

			answer2=null;

			remainder2=0;

			if(bot2!=null) {
				bot2=bot2.next;
			}


		}
		if((first.negative==false && second.negative==false) || (first.negative==true && second.negative==true)) {
			ans.negative=false;
		}else if((first.negative==false && second.negative==true) ||(second.negative==false && first.negative==true)) {
			ans.negative=true;
		}

	


		String tempor=ans.toString();

		ans=parse(tempor);


		return ans;
	}



	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (front == null) {
			return "0";
		}
		String retval = front.digit + "";
		for (DigitNode curr = front.next; curr != null; curr = curr.next) {
			retval = curr.digit + retval;
		}

		if (negative) {
			retval = '-' + retval;
		}
		return retval;
	}

}
