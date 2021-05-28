package Ej6a;
import java.sql.*;
import java.util.LinkedList;
import java.util.Scanner;


public class Principal { 

	@SuppressWarnings("deprecation")
	public static void main(String[] args)  {
		// registrar el conector
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			
			e.printStackTrace();
		}
		
		
		//getAll();
		
		//getOne();
		
		//insertNew();
		
		//delete();
		
		//update();
		

		System.out.println("\n -- FIN DEL PROGRAMA -- ");

	}
	private static void getAll() {
		Connection conn = null;
		LinkedList<Product> productos = new LinkedList<>();
		try {
		//		crear una conexion
			conn = 
				DriverManager.getConnection("jdbc:mysql://localhost/javamarket","root","Camichaca12");
			
		//		ejecutar la query
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from product ");
			
		//		mapear resulset a objeto
			while(rs.next()) {
				Product p = new Product();
				p.setId(rs.getInt("id"));
				p.setNombre(rs.getString("nombre"));
				p.setDescripcion(rs.getString("descripcion"));
				p.setPrecio(rs.getDouble("precio"));
				p.setStock(rs.getInt("stock"));
				p.setShippingIncluded(rs.getBoolean("shippingIncluded"));
				
				productos.add(p);
			}
		//		cerrar recursos
			if(rs != null){rs.close();}
			if(stmt != null){stmt.close();}
			conn.close();
			
		//		mostrar info
			System.out.println("Listado de Productos");
			System.out.println(productos);
			System.out.println();System.out.println();
			
			} catch(SQLException ex){
		
		//		manejo de errores
				System.out.println("SQLException:" + ex.getMessage());
				System.out.println("SQLState:" + ex.getSQLState());
				System.out.println("VendorError: " + ex.getErrorCode());
				
			}

	}
	
	private static void getOne() {
		Connection conn = null;
		try {
		//		Creamos la Coneccion
		conn = 
				DriverManager.getConnection("jdbc:mysql://localhost/javamarket","root","Camichaca12");
		
		//		definimos la query
		PreparedStatement stmt = conn.prepareStatement("select * from product where id=?");
		
		//		setear el / los parametros 
		Scanner entrada = new Scanner(System.in);
		System.out.print("Ingrese ID del producto que desea visualizar: ");
		int idIngresado = entrada.nextInt();
		stmt.setInt(1, idIngresado);
		Product p = new Product();
		
		//		ejecutar query y obtener resultados
		ResultSet rs = stmt.executeQuery();
		
		//		mapear de result set a objeto
		if(rs.next()) {
			p.setId(rs.getInt("id"));
			p.setNombre(rs.getString("nombre"));
			p.setDescripcion(rs.getString("descripcion"));
			p.setPrecio(rs.getDouble("precio"));
			p.setStock(rs.getInt("stock"));
			p.setShippingIncluded(rs.getBoolean("shippingIncluded"));
			
		}
		
		// 		cerrar recursos 		
		if(rs!=null){rs.close();}
		if(stmt!=null){stmt.close();}
		conn.close();
		entrada.close();
		
		// 		mostrar el objeto
		String envio ;
		System.out.println("\n Buscar por ID");
		System.out.println(p);
		if(p.getShippingIncluded() == true) {
			envio = "SI";
		}else {
			envio = "NO";
		}
		System.out.println(" Descripcion = " + p.getDescripcion() +
							"\n Stock = " + p.getStock() +
							"\n Envio Incluido = " + envio);
		System.out.println();System.out.println();
		} catch(SQLException ex){
		
		// 		manejo de errores
			System.out.println("SQLException:" + ex.getMessage());
			System.out.println("SQLState:" + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}
	
	public static void insertNew() {
		
		Scanner entrada = new Scanner(System.in);

		

			Connection conn = null;
			Product pIns = new Product();
			
			System.out.print("Ingrese NOMBRE del nuevo producto: ");
			String newNombre = entrada.next() ;
			pIns.setNombre(newNombre);
			
			System.out.print("Ingrese DESCRIPCION del nuevo producto: ");
			String newDescripcion = entrada.next();
			pIns.setDescripcion(newDescripcion);
			
			System.out.print("Ingrese PRECIO del nuevo producto: ");
			double newPrecio = entrada.nextDouble() ;
			pIns.setPrecio(newPrecio);
			
			System.out.print("Ingrese STOCK del nuevo producto: ");
			int newStock = entrada.nextInt() ;
			pIns.setStock(newStock);
			
			System.out.print("El nuevo producto, ¿Posee envio incluido?"
					+ "\n 1. SI"
					+ "\n 2. NO"
					+ "\n Ingrese opcion: ");
			int op = entrada.nextInt();
			boolean newShipInc;
			if(op == 1) {
				 newShipInc = true;
			}else {
				 newShipInc = false;
			}
			
			pIns.setShippingIncluded(newShipInc);	
						
					//		 	--- TRY CATCH ---
					//	--- TRY CATCH ---
					try {
						//crear una conexion
						conn = 
								DriverManager.getConnection("jdbc:mysql://localhost/javamarket","root","Camichaca12");
						
					
						// definir la query
						PreparedStatement pstmt = conn.prepareStatement(
								"insert into product(nombre,descripcion,precio,stock,shippingIncluded) values (?,?,?,?,?)"
								,PreparedStatement.RETURN_GENERATED_KEYS
								);
						pstmt.setString(1, pIns.getNombre());
						pstmt.setString(2, pIns.getDescripcion());
						pstmt.setDouble(3, pIns.getPrecio());
						pstmt.setInt(4, pIns.getStock());
						pstmt.setBoolean(5, pIns.getShippingIncluded());
						
						pstmt.executeUpdate();
						
						
						
						ResultSet keyResultSet=pstmt.getGeneratedKeys();
						
						if(keyResultSet != null && keyResultSet.next()) {
							int id = keyResultSet.getInt(1);
							System.out.println("\n ID: " + id);
							pIns.setId(id);
							
						}
						
						if(keyResultSet != null) {keyResultSet.close();}
						if(pstmt != null) {pstmt.close();}
						
						conn.close();
						//Mostrar objeto
						System.out.println("\n Nuevo Producto");
						System.out.println(pIns);
						System.out.println();System.out.println();
						
					}catch(SQLException ex) {
						//manejo de errores
						System.out.println("SQLException:" + ex.getMessage());
						System.out.println("SQLState:" + ex.getSQLState());
						System.out.println("VendorError: " + ex.getErrorCode());
					}		
					
		
		//	--- TRY CATCH ---
		// 	--- TRY CATCH ---
		System.out.println("\n PRODUCTO AGREGADO CORRECTAMENTE");
		
		// cierre entrada scanner
		entrada.close();
		
	}
	
	public static void delete() {
		Connection conn = null;
		Scanner entrada = new Scanner(System.in);
		
		System.out.print("Ingrese ID del producto que desea eliminar: ");
		int idDelete = entrada.nextInt() ;
		
		
		try {
			conn = 
					DriverManager.getConnection("jdbc:mysql://localhost/javamarket","root","Camichaca12");
			// definir la query
			PreparedStatement pstmt = conn.prepareStatement(
					"DELETE FROM product WHERE id='"+ idDelete + "'");
			pstmt.executeUpdate();
			
			if(pstmt != null) {pstmt.close();}
			
			conn.close();
					
		
		}catch(SQLException ex){
			//manejo de errores
			System.out.println("SQLException:" + ex.getMessage());
			System.out.println("SQLState:" + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		System.out.println("\nPRODUCTO ELIMINADO CORRECTAMENTE");
		
		// cierre entrada scanner
		entrada.close();
	}

	public static void update() {
		Connection conn = null;
		Scanner entrada = new Scanner(System.in);			
		
		try {
			//crear una conexion
			conn = 
				DriverManager.getConnection("jdbc:mysql://localhost/javamarket","root","Camichaca12");
			//	definimos la query
			PreparedStatement stmt = conn.prepareStatement("select * from product where id=?");
			
			//			setear el parametro
			System.out.print("Ingrese ID del producto que desea ACTUALIZAR: ");
			int idIngresado = entrada.nextInt();
			stmt.setInt(1, idIngresado);
			Product p = new Product();
			
			//			ejecutar query y obtener resultados
			ResultSet rs = stmt.executeQuery();
			
			//		mapear de result set a objeto
			if(rs.next()) {
				p.setId(rs.getInt("id"));
				p.setNombre(rs.getString("nombre"));
				p.setDescripcion(rs.getString("descripcion"));
				p.setPrecio(rs.getDouble("precio"));
				p.setStock(rs.getInt("stock"));
				p.setShippingIncluded(rs.getBoolean("shippingIncluded"));
			}
			//	 		cerrar recursos 		
			if(rs!=null){rs.close();}
			if(stmt!=null){stmt.close();}
			
			
			//	 		mostrar el objeto
			String envio ;
			System.out.println("\n Buscar por ID");
			System.out.println(p);
			if(p.getShippingIncluded() == true) {
				envio = "SI";
			}else {
				envio = "NO";
			}
			System.out.println(" Descripcion = " + p.getDescripcion() +
								"\n Stock = " + p.getStock() +
								"\n Envio Incluido = " + envio);
			System.out.println();System.out.println();
			
			System.out.print("Ingrese nuevo NOMBRE del producto: ");
			String nombreUpdate = entrada.next();
			System.out.print("Ingrese nueva DESCRIPCION del producto: ");
			String descripcionUpdate = entrada.next();
			System.out.print("Ingrese nuevo PRECIO del producto: ");
			double precioUpdate = entrada.nextDouble();
			System.out.print("Ingrese nuevo STOCK del producto: ");
			int stockUpdate = entrada.nextInt();
			System.out.println("¿Posee envio incluido?"
					+ "\n 1. SI"
					+ "\n 2. NO"
					+ "\n Ingrese opcion: ");
			int op = entrada.nextInt();
			int shipIncUpdate;
			if(op == 1) {
				shipIncUpdate = 1;
			}else {
				shipIncUpdate = 0;
			}
			
			
			// definir la query
				PreparedStatement pstmt = conn.prepareStatement(
						"UPDATE product SET nombre = '" + nombreUpdate +"', descripcion = '"+descripcionUpdate+"', precio ='"+precioUpdate+"',stock ='"+stockUpdate+"',shippingIncluded = '" + shipIncUpdate +"' WHERE id ="+idIngresado);
				
						
				pstmt.executeUpdate();	
			
				
								

				if(pstmt != null) {pstmt.close();}
				
				conn.close();
			
			
		} catch(SQLException ex) {
			//manejo de errores
			System.out.println("SQLException:" + ex.getMessage());
			System.out.println("SQLState:" + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		
		System.out.print("--- Producto actualizado correctamente --- ");
		
		
		// cierre entrada scanner
				entrada.close();
	}
}
