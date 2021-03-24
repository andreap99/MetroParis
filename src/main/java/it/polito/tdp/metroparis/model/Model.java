package it.polito.tdp.metroparis.model;

import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import it.polito.tdp.metroparis.db.MetroDAO;

public class Model {
	
	private Graph<Fermata, DefaultEdge> graph;
	private Map<Integer, Fermata> fermate;
	
	public Model() {
		this.graph = new SimpleDirectedGraph<>(DefaultEdge.class);
		MetroDAO dao = new MetroDAO();
		this.fermate = dao.getAllFermate();
		Graphs.addAllVertices(graph, fermate.values());
		/*
		//Creazione degli archi --- metodo 1 (coppia di vertici)
		for(Fermata fp : this.fermate.values()) {
			for(Fermata fa : this.fermate.values()) {
				if(dao.fermateConnesse(fp, fa)) {
					this.graph.addEdge(fp, fa);
				}
			}
		}*/
		//System.out.println(this.graph);
		
		/*
		//Creazione degli archi --- metodo 2 (da un vertice, trova tutti quelli connessi)
		for(Fermata fp : this.fermate.values()) {
			List<Fermata> connesse = dao.fermateSuccessive(fp, fermate);
			for(Fermata fa : connesse) {
				this.graph.addEdge(fp, fa);
			}
		}
		*/
		//Creazione degli archi --- metodo 3 (chiedo al db l'elenco degli archi)
		List<Coppia> coppie = dao.coppieFermate(fermate);
		for(Coppia c : coppie) {
			this.graph.addEdge(c.getPartenza(),  c.getArrivo());
		}
		
		System.out.format("Grafo caricato con %d vertici e %d archi", this.graph.vertexSet().size(), 
			this.graph.edgeSet().size());
	}
	
	public static void main(String args[]) {
		Model m = new Model();
	}

}
