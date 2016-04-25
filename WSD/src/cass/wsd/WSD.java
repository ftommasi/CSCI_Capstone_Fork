package cass.wsd;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;

import cass.languageTool.*;
import cass.languageTool.wordNet.CASSWordSense;

public class WSD {
	
	private LanguageTool lTool;
	private List<String> context;
	@SuppressWarnings("unused")
	private String target;
	Set<CASSWordSense> targetSenses;
	
	public WSD(String leftContext, String target, String rightContext, Language language) {
		lTool = new LanguageTool(language);
		
		this.target = target;
		
		context = new ArrayList<String>();
		context.addAll(lTool.tokenizeAndLemmatize(leftContext));
		context.addAll(lTool.tokenizeAndLemmatize(rightContext));
		
		targetSenses = lTool.getSenses(target);
		}
	
	public List<CASSWordSense> rankSensesUsing(Algorithm algorithm) {
		
		List<ScoredSense> scoredSenses = scoreSensesUsing(algorithm);
		List<CASSWordSense> rankedSenses = new ArrayList<CASSWordSense>();
		
		// convert ScoredSense to WordSense, discard score
		for (ScoredSense wordSense : scoredSenses) {
			rankedSenses.add(wordSense.getSense());
		}
		
		return rankedSenses;
	}
	
	List<ScoredSense> scoreSensesUsing(Algorithm algorithm) {
		
		List<ScoredSense> scoredSenses;
		
		switch (algorithm) {
		case LESK:
			scoredSenses = scoreSensesUsingLesk();
			break;

		case STOCHASTIC_GRAPH:
			scoredSenses = scoreSensesUsingStochasticHypernymDistance();
			break;
			
		case FREQUENCY:
			scoredSenses = scoreSensesUsingTagFrequency();
			break;
			
		case RANDOM:
			scoredSenses = scoreSensesRandomly();
			break;
			
		default:
			// TODO throw proper exception
			return null;
		}
		
		return scoredSenses;
	}
	
	private List<ScoredSense> scoreSensesUsingLesk() {
		
		// context set is set of words in context
		Set<String> contextSet = new HashSet<String>(context);
		
		Set<String> glossSet = new HashSet<String>();
		List<ScoredSense> scoredSenses= new ArrayList<ScoredSense>();
		
		// for every set of synonyms in the list
		for (CASSWordSense targetSense : targetSenses) {
			// clear and add lemmatized tokens of gloss to set
			glossSet.clear();
			String definition = lTool.getDefinition(targetSense);
			glossSet.addAll(lTool.tokenizeAndLemmatize(definition));
			
			// find intersection of sets
			glossSet.retainAll(contextSet);
			
			// score is cardinality of intersection
			int score = glossSet.size();
			
			scoredSenses.add(new ScoredSense(targetSense, score));
		}
		
		// sort in descending order
		Collections.sort(scoredSenses);
		Collections.reverse(scoredSenses);
		
		return scoredSenses;
	}
	
	private List<ScoredSense> scoreSensesUsingStochasticHypernymDistance() {
		List<ScoredSense> scoredSenses= new ArrayList<ScoredSense>();
		
		for (CASSWordSense targetSense : targetSenses) {
			int senseScore = 0;
		
			for (String contextWord : context) {
				
				// for each sense of the current context word, find the sense with the minimum distance to the current target sense
				Set<CASSWordSense> contextWordSenses = lTool.getSenses(contextWord);
				
				int bestScore = Integer.MAX_VALUE;
				for (CASSWordSense contextWordSense : contextWordSenses) {
					
					int currentScore = lTool.getHypernymDistanceScore(targetSense, contextWordSense);
					if (currentScore < bestScore) {
						bestScore = currentScore;
					}
				}
				senseScore += bestScore;
			}
			scoredSenses.add(new ScoredSense(targetSense, senseScore));
		}
		
		// sort in ascending order
		Collections.sort(scoredSenses);
		
		return scoredSenses;
	}
	
	private List<ScoredSense> scoreSensesUsingTagFrequency() {
		List<ScoredSense> scoredSenses= new ArrayList<ScoredSense>();
				
		for (CASSWordSense sense : targetSenses) {
			scoredSenses.add(new ScoredSense(sense, sense.getTagFrequency()));
		}

		Collections.sort(scoredSenses);
		Collections.reverse(scoredSenses);
		
		return scoredSenses;
	}
	
	private List<ScoredSense> scoreSensesRandomly() {
		Random rand = new Random();
		
		List<ScoredSense> scoredSenses= new ArrayList<ScoredSense>();
		
		for (CASSWordSense sense : targetSenses) {
			scoredSenses.add(new ScoredSense(sense, rand.nextInt()));
		}

		Collections.sort(scoredSenses);
		Collections.reverse(scoredSenses);
		
		return scoredSenses;
	}
}