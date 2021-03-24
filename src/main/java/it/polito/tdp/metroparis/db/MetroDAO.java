package it.polito.tdp.metroparis.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.javadocmd.simplelatlng.LatLng;

import it.polito.tdp.metroparis.model.Coppia;
import it.polito.tdp.metroparis.model.Fermata;
import it.polito.tdp.metroparis.model.Linea;

public class MetroDAO {

	public Map<Integer, Fermata> getAllFermate() {

		final String sql = "SELECT id_fermata, nome, coordx, coordy FROM fermata ORDER BY nome ASC";
		Map<Integer, Fermata> fermate = new HashMap<>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Fermata f = new Fermata(rs.getInt("id_Fermata"), rs.getString("nome"),
						new LatLng(rs.getDouble("coordx"), rs.getDouble("coordy")));
				fermate.put(f.getIdFermata(), f);
			}

			st.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore di connessione al Database.");
		}

		return fermate;
	}

	public List<Linea> getAllLinee() {
		final String sql = "SELECT id_linea, nome, velocita, intervallo FROM linea ORDER BY nome ASC";

		List<Linea> linee = new ArrayList<Linea>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Linea f = new Linea(rs.getInt("id_linea"), rs.getString("nome"), rs.getDouble("velocita"),
						rs.getDouble("intervallo"));
				linee.add(f);
			}

			st.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore di connessione al Database.");
		}

		return linee;
	}
	
	public boolean fermateConnesse(Fermata fp, Fermata fa) {
		String sql = "SELECT COUNT(*) AS C FROM connessione \n" + 
				"WHERE id_stazP = ? AND id_stazA = ?";
		
		try {
			
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, fp.getIdFermata());
			st.setInt(2, fa.getIdFermata());
			ResultSet rs = st.executeQuery();
			
			rs.first();
			int linee = rs.getInt("C");
			
			conn.close();
			return linee>=1;
			
		}catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<Fermata> fermateSuccessive(Fermata f, Map<Integer, Fermata> fermate){
		String sql = "SELECT distinct id_stazA FROM connessione WHERE id_stazP = ?";
		List<Fermata> result = new ArrayList<>();
		try {
			
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, f.getIdFermata());
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				result.add(fermate.get(rs.getInt("id_stazA")));
			}
			
			conn.close();
			return result;
			
		}catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public List<Coppia> coppieFermate(Map<Integer, Fermata> fermate) {
		String sql = "SELECT DISTINCT  id_stazP, id_stazA FROM connessione";
		List<Coppia> result = new ArrayList<>();
		try {
			
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				Coppia c = new Coppia(fermate.get(rs.getInt("id_stazP")), fermate.get(rs.getInt("id_stazA")));
				result.add(c);
			}
			
			conn.close();
			return result;
			
		}catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
