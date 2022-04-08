package com.nawak.dirbox;

import java.nio.file.Path;
import java.util.function.Predicate;


public class DirBoxBuilder {

	private final DirBox dirBox;

	public DirBoxBuilder(Path directory) {
		this.dirBox = new DirBox(directory);
	}

	public DirBoxBuilder withPredicate(Predicate<String> predicate) {
		this.dirBox.setPredicate(predicate);
		return this;
	}

	public DirBoxBuilder withPattern(String pattern) {
		this.dirBox.setPattern(pattern);
		return this;
	}

	public DirBoxBuilder type(Type type) {
		this.dirBox.setType(type);
		return this;
	}

	public DirBoxBuilder maxDepth(int maxDepth) {
		this.dirBox.setMaxDepth(maxDepth);
		return this;
	}

	public DirBoxBuilder keepStructure(boolean keepStructure) {
		this.dirBox.setKeepStructure(keepStructure);
		return this;
	}

	//TODO : bof d'appeler ça build alors que ça renvoit uniquement l'objet à changer !!
	public DirBox build() {
		return this.dirBox;
	}

}
