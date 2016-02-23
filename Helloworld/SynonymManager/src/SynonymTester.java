import java.util.ArrayList;

import edu.smu.tspell.wordnet.Synset;

/** Author(s)/Contributor(s): Fausto Tommasi
 *  Date: 2/21/2016
 *  Purpose: Test validity of SynonymManager classes
 */
public class SynonymTester {
	public static void main(String[] args){
		String key = "wordnet.database.dir";
		String value = "/home/design/Documents/WordNet-3.0/dict/"; 
		System.setProperty(key, value);//initialize the Synset database
		//TODO(TEAM): write tests for synonym Manager classes
		SynonymGetter s= new SynonymGetter();
		ArrayList<ArrayList<String>> mine = s.getSynonym("food");
		for(int i=0; i<mine.size(); i++){
			for(int j=0; j < mine.get(i).size(); j++){
				String output = mine.get(i).get(j);
				System.out.println(output);
			}
			
		}
	}

}
