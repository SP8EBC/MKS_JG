package pl.jeleniagora.mks.events;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import pl.jeleniagora.mks.rte.RTE_ST;

/**
 * Klasa zawierąca metody wywoływane po ostatim saneczkarzu w ślizgu
 * @author mateusz
 *
 */
public class EndOfRun {

	static AnnotationConfigApplicationContext ctx;

	/**
	 * Statyczna metoda ustawiająca referencję na kontekst aplikacji. Kontekst jest jedynym stanem przechowywanym
	 * wewnątrz klasy. Nie zmienia to jednak faktu, że klasa i jej metody ma dość dużo side-effect
	 * @param context
	 */
	public static void setAppCtx(AnnotationConfigApplicationContext context) {
		ctx = context;
	}
	
	private EndOfRun() {
		
	}
	
	public static void switchToNextRun() {
		
		RTE_ST rte_st = (RTE_ST)ctx.getBean("RTE_ST");
	}
}
