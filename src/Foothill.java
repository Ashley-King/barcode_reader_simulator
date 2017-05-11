/*
 * Course:   CS 1B Fall 2016
 * Student: Ashley King
 * Program: Optical Barcode Readers and Writers
 */

public class Foothill
{

   public static void main(String[] args)
   {
      String[] sImageIn = 
            { 
                     "                                      ",
                     "                                      ",
                     "                                      ",
                     "* * * * * * * * * * * * * * * * *     ",
                     "*                                *    ",
                     "**** * ****** ** ****** *** ****      ",
                     "* ********************************    ",
                     "*    *   *  * *  *   *  *   *  *      ",
                     "* **    *      *   **    *       *    ",
                     "****** ** *** **  ***** * * *         ",
                     "* ***  ****    * *  **        ** *    ",
                     "* * *   * **   *  *** *   *  * **     ",
                     "**********************************    "
            };

      String[] sImageIn_2 = 
            { 
                     "                                          ",
                     "                                          ",
                     "* * * * * * * * * * * * * * * * * * *     ",
                     "*                                    *    ",
                     "**** *** **   ***** ****   *********      ",
                     "* ************ ************ **********    ",
                     "** *      *    *  * * *         * *       ",
                     "***   *  *           * **    *      **    ",
                     "* ** * *  *   * * * **  *   ***   ***     ",
                     "* *           **    *****  *   **   **    ",
                     "****  *  * *  * **  ** *   ** *  * *      ",
                     "**************************************    "
            };

      //create BarcodeImage and DataMatrix objects
      BarcodeImage bc = new BarcodeImage(sImageIn);
      DataMatrix dm = new DataMatrix(bc);

      //First Secret Message
      System.out.println("First secret message: ");
      dm.translateImageToText();
      dm.displayTextToConsole();
      System.out.println();
      dm.displayImageToConsole();
      System.out.println();

      //Second Secret Message
      System.out.println("Second secret message: ");
      bc = new BarcodeImage(sImageIn_2);
      dm.scan(bc);
      dm.translateImageToText();
      dm.displayTextToConsole();
      System.out.println();
      dm.displayImageToConsole();
      System.out.println();
      
      //Create own message
      System.out.println("Third secret message: ");
      dm.readText("hot diggity dog!");
      dm.generateImageFromText();
      dm.displayTextToConsole();
      System.out.println();
      dm.displayImageToConsole();

   }//end main

}//end foothill
//interface BarcodeIO **************************
interface BarcodeIO
{
   public abstract boolean scan(BarcodeImage bc);
   public abstract boolean readText(String text);
   public abstract boolean generateImageFromText();
   public abstract boolean translateImageToText();
   public abstract void displayTextToConsole();
   public abstract void displayImageToConsole();
}//end interface BarcodeIO
// class BarcodeImage **********************
class BarcodeImage implements Cloneable
{
   //data
   public static final int MAX_HEIGHT = 30;
   public static final int MAX_WIDTH = 65;
   private boolean[][] image_data;

   //methods
   //default constructor
   BarcodeImage()
   {
      //instantiate image_data array
      int row, col;
      image_data = new boolean[MAX_HEIGHT][MAX_WIDTH];
      //fill array
      for(row = MAX_HEIGHT-1; row >= 0; row--)
      {
         for(col = 0; col < MAX_WIDTH; col++)
         {
            setPixel(row,col,false);
         }
      }

   }
   //parameter constructor
   BarcodeImage(String[] str_data)
   {
      //instantiate image_data array
      int row, col;
      image_data = new boolean[MAX_HEIGHT][MAX_WIDTH];
      //if checkSize fails, return immediately
      if(!checkSize(str_data))
      {
         System.out.println("String data incorrect size");
         return;
      }
      else
      {
         //set up index
         int last = 0;  
         last = str_data.length;
         last -= 1;
         int index = last;
         //loop through str_data[] & image_data[][]
         for(row = MAX_HEIGHT -1; row >= 0; row--)
         {
            if(index < 0)
            {
               break;
            }
            //get last element of str data
            String newStr = str_data[index];
            //convert to boolean array
            boolean [] boolArray = new boolean[MAX_WIDTH];
            boolArray = newBoolArray(newStr);
            //load into data
            for(col = 0; col < MAX_WIDTH; col++)
            {
               if(col < boolArray.length-1)
               {
                  setPixel(row,col,boolArray[col]);
               }
            }//end for col
            index--;
         }//end for row
      }

   }
   //clone method
   public BarcodeImage clone() throws CloneNotSupportedException
   {
      BarcodeImage newImage = (BarcodeImage)super.clone();
      return newImage;
   }
   //getters & setters
   public boolean getPixel(int row, int col)
   {
      return image_data[row][col];
   }
   public boolean setPixel(int row, int col, boolean value)
   {
      if(row >= 0 && row < MAX_HEIGHT && col >= 0 && col < MAX_WIDTH)
      {
         image_data[row][col] = value;
         return true;
      }
      else
      {
         return false;
      }
   }
   //helper methods
   private boolean checkSize(String[] data)
   {
      //get num of rows and check width of cols
      int rowCount = 0;
      int colError = 0;
      for(int i = 0; i < data.length; i++)
      {
         rowCount++;
         if(data[i].length() >= MAX_WIDTH || data[i] == null)
         {
            colError++;
         }
      }
      if(rowCount > 0 && rowCount < MAX_HEIGHT && colError == 0)
      {
         return true;
      }
      else
      {
         return false;
      }
   }
   public void displayToConsole()
   {
      int row,col;
      for(row = 0; row < MAX_HEIGHT; row++)
      {
         for(col = 0; col < MAX_WIDTH; col++ )
         {

            if(image_data[row][col] == false)
            {
               System.out.print(" ");
            }
            else
            {
               System.out.print("*");
            }
         }
         System.out.println();
      }//end for to print 2d array
   }
   //method to convert chars to booleans
   private boolean[] newBoolArray(String str)
   {
      //instantiate new char and new boolean
      char newChar;
      boolean newBool;
      //create boolean array
      boolean[] boolArray = new boolean[MAX_WIDTH];
      //loop through str
      for(int i=0; i< str.length(); i++)
      {
         //find first char
         newChar = str.charAt(i);
         //convert it to boolean
         if(newChar == '*')
         {
            newBool = true;
         }
         else
         {
            newBool = false;
         }
         //put it in boolean array
         boolArray[i] = newBool;
      }
      //return boolean array
      return boolArray;
   }
}//end class BarcodeImage
//class DataMatrix **********************
class DataMatrix implements BarcodeIO
{
   //data
   public static final char BLACK_CHAR = '*';
   public static final char WHITE_CHAR = ' ';
   private BarcodeImage image;
   private String text;
   private int actualWidth, actualHeight;
   public static final String DEFAULT_TEXT = "undefined";

   //constructors
   DataMatrix()
   {
      //empty but non-null image and text value
      this.image = new BarcodeImage();
      readText(DEFAULT_TEXT);
   }
   DataMatrix(BarcodeImage image)
   {
      //sets image but leaves text at default
      //call scan()
      if(!scan(image))
      {
         //if scan fails, create blank image
         this.image = new BarcodeImage();
      }
      readText(DEFAULT_TEXT);
   }
   DataMatrix(String text)
   {
      this.image = new BarcodeImage();
      if(!readText(text))
      {
         this.text = DEFAULT_TEXT;
      }

   }
   //getters and setters
   public boolean readText(String text)
   {
      boolean bool;
      if(text != null)
      {
         this.text = text;
         bool = true;
      }
      else
      {
         bool = false;
      }
      //placeholder

      return bool;
   }
   public boolean scan(BarcodeImage image)
   {
      boolean bool;
      //call clone of BarcodeImage inside try catch
      try
      {
         this.image = image.clone();
         bool = true;
      }catch(CloneNotSupportedException e)
      {
         System.out.println("Clone not supported");
         bool = false;
      }
      //set actual height/width
      this.actualWidth = computeSignalWidth();
      this.actualHeight = computeSignalHeight();
      return bool;
   }

   public int getActualWidth()
   {
      return actualWidth;
   }
   public int getActualHeight()
   {
      return actualHeight;
   }
   private int computeSignalWidth()
   {
      int width = 0;
      for(int i = 0; i < BarcodeImage.MAX_WIDTH; i++)
      {
         if(this.image.getPixel(BarcodeImage.MAX_HEIGHT-1, i) == true)
         {
            width++;
         }
      }
      return width;
   }

   private int computeSignalHeight()
   {
      int height = 0;
      for(int i = BarcodeImage.MAX_HEIGHT-1; i >= 0; i--)
      {
         if(this.image.getPixel(i, 0) == true)
         {
            height++;
         }
      }
      return height;
   }
   //helper methods
   public boolean translateImageToText()
   {
      //loop through image data
      int col;
      int index = 0;
      char letter;
      char[] newText = new char[BarcodeImage.MAX_WIDTH-1];

      for(col = 1; col < actualWidth -1; col++)
      {
         //get chars from column
         letter = readCharFromCol(col);
         //create an array of * and ' '
         newText[index] = letter;
         index++;
      }
      //translate letter array to string
      String newTextString = new String(newText);
      //set text
      readText(newTextString);
      //return true
      return true;
   }
   private char readCharFromCol(int col)
   {
      int row;
      int index = 0;
      char newPixel = WHITE_CHAR;
      char newLetter;
      char newCharArray[] = new char[actualHeight - 2];
      for(row = BarcodeImage.MAX_HEIGHT - (actualHeight -1); 
               row < BarcodeImage.MAX_HEIGHT -1; row++ )
      {
         if(image.getPixel(row, col) == true)
         {
            newPixel =  BLACK_CHAR;
         }
         else
         {
            newPixel = WHITE_CHAR;
         }
         newCharArray[index] = newPixel;
         index++;
         
      }
      newLetter = readLetterFromArray(newCharArray);
      return newLetter;
   }
   //translate array of chars from image into single alphanumeric char
   private char readLetterFromArray(char[] chars)
   {
      int sum = 0;
      int index = chars.length - 1;
      char newChar;
      for(int i = 0; i < chars.length; i++)
      {
         if(chars[i] == BLACK_CHAR)
         {
            sum = sum + (int) Math.pow(2,index);
         }
         index--;
      }
      newChar = (char)sum;
      //return alphanumeric character
      return newChar;
   }
   public boolean generateImageFromText()
   {
      //convert char to int to bin
      int newInt;
      int standard_height = 10;
      //create new string data array
      String[]newImageData = new String[text.length()];
      newImageData = createStringData(standard_height, text.length() + 1);
      //create new BarcodeImage
      BarcodeImage newImage = new BarcodeImage(newImageData);
      scan(newImage);
      //convert text
      for(int i = 0; i < text.length(); i ++)
      {
         //convert char to int
         newInt = (int)text.charAt(i);
         //write to col
         writeToCol(i+1, newInt);
      }
      return true;
   }//end generateImageFromText
   private boolean writeToCol(int col, int code)
   {
      //convert to binary string
      String bin = Integer.toBinaryString(code); 
      //make binary string 8 bits
      String leadingBit = "0";
      if(bin.length() == 6)
      {
         bin = leadingBit + leadingBit + bin;
      }
      if(bin.length() == 7)
      {
         bin = leadingBit + bin;
      }
      //convert binary to array of * & spc; add last bit
      boolean[] pixels = new boolean[bin.length()+1];
      pixels = convertBinToBooleanArray(bin); 
      //write to col
      int row;
      int index = 0;
      for(row = BarcodeImage.MAX_HEIGHT - (actualHeight - 1); 
               row < BarcodeImage.MAX_HEIGHT - 1; row++ )
      {
         image.setPixel(row, col, pixels[index]);
         index++;
      }
      return true;
   }
   private String[] createStringData(int height,int length)
   {
      String[] newData = new String[height];
      for(int i = 0; i < height; i ++)
      {
         newData[i] = "";
      }
      //top and bottom limitation lines
      for(int i = 0; i < length; i++)
      {
         newData[0] += String.valueOf(BLACK_CHAR);
         newData[height-1] += String.valueOf(BLACK_CHAR);
      }
      //fill in rest
      for(int i = 1; i< height - 1; i++)
      {
         newData[i] = String.valueOf(BLACK_CHAR);
         for(int k = 1; k < length; k++)
         {
            newData[i] += String.valueOf(WHITE_CHAR);
         }
      }
      return newData;
   }
   private boolean[] convertBinToBooleanArray(String binary)
   {
      int bits = 8;
      boolean[] boolArray = new boolean[bits];
      
      for(int i = 0; i < binary.length(); i++)
      {
         if(binary.charAt(i) == '1')
         {
            boolArray[i] = true;
         }
         else
         {
            boolArray[i] = false;
         }
      }
      return boolArray;
   }

   public void displayTextToConsole()
   {
      System.out.println(this.text.toUpperCase());
   }
   public void displayImageToConsole()
   {
      int i,k;
      //print top border
      for(i = 0; i <= actualWidth + 2; i++)
      {
         System.out.print("-");
      }
      System.out.println();
      //print image
      for(i = (BarcodeImage.MAX_HEIGHT) - actualHeight; 
               i < BarcodeImage.MAX_HEIGHT; i ++)
      {
         System.out.print("|");
         for(k = 0; k <= actualWidth; k++)
         {
            if(this.image.getPixel(i, k) == true)
            {
               System.out.print(BLACK_CHAR);
            }
            else
            {
               System.out.print(WHITE_CHAR);
            } 
         }
         System.out.println("|");
      }


   }
}//end class DataMatrix

/************ OUTPUT ***************
First secret message: 
DON'T FORGET TO REMOVE THE TABS!                                

-------------------------------------
|* * * * * * * * * * * * * * * * *  |
|*                                * |
|**** * ****** ** ****** *** ****   |
|* ******************************** |
|*    *   *  * *  *   *  *   *  *   |
|* **    *      *   **    *       * |
|****** ** *** **  ***** * * *      |
|* ***  ****    * *  **        ** * |
|* * *   * **   *  *** *   *  * **  |
|********************************** |

Second secret message: 
YOU DID IT!  GREAT WORK.  CELEBRATE.                            

-----------------------------------------
|* * * * * * * * * * * * * * * * * * *  |
|*                                    * |
|**** *** **   ***** ****   *********   |
|* ************ ************ ********** |
|** *      *    *  * * *         * *    |
|***   *  *           * **    *      ** |
|* ** * *  *   * * * **  *   ***   ***  |
|* *           **    *****  *   **   ** |
|****  *  * *  * **  ** *   ** *  * *   |
|************************************** |

Third secret message: 
HOT DIGGITY DOG!

--------------------
|***************** |
|*                 |
|**** ******* ***  |
|***************** |
|*  *      **      |
|***   *  * *  *   |
|* ** * ** *  ***  |
|* *    **     **  |
|* *   **** *  *** |
|***************** |

*/