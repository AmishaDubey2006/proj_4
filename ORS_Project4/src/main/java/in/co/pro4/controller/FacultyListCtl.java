package in.co.pro4.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.pro4.bean.BaseBean;
import in.co.pro4.bean.FacultyBean;
import in.co.pro4.exception.ApplicationException;
import in.co.pro4.model.CollegeModel;
import in.co.pro4.model.CourseModel;
import in.co.pro4.model.FacultyModel;
import in.co.pro4.utility.DataUtility;
import in.co.pro4.utility.PropertyReader;
import in.co.pro4.utility.ServletUtility;

/**
 * Faculty List functionality Controller. 
 * Performs operation for list, search and delete operations of Faculty
 * 
 *
 */



/**
 * Servlet implementation class FacultyListCtl
 */
/**
 * @author Amisha Dubey
 *
 */
@WebServlet(name = "FacultyListCtl", urlPatterns = { "/ctl/FacultyListCtl" })
public class FacultyListCtl extends BaseCtl {
	private static Logger log = Logger.getLogger(FacultyListCtl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * in.co.pro4.controller.BaseCtl#preload(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected void preload(HttpServletRequest request) {

		CollegeModel cmodel = new CollegeModel();
		CourseModel comodel = new CourseModel();

		try {
			List clist = cmodel.list();
			List colist = comodel.list();

			request.setAttribute("CollegeList", clist);
			request.setAttribute("CourseList", colist);
		} catch (ApplicationException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see in.co.pro4.controller.BaseCtl#populatebean(javax.servlet.http.
	 * HttpServletRequest)
	 */
	protected BaseBean populateBean(HttpServletRequest request) {

		FacultyBean bean = new FacultyBean();

		bean.setFirstName(DataUtility.getString(request.getParameter("firstname")));
		bean.setLastName(DataUtility.getString(request.getParameter("lastname")));
		bean.setEmailId(DataUtility.getString(request.getParameter("login")));
		// System.out.println("in populate ------------");
		bean.setCollegeId(DataUtility.getLong(request.getParameter("collegeid")));
		// System.out.println(bean.getCollegeId());
		bean.setCourseId(DataUtility.getLong(request.getParameter("courseid")));
		System.out.println(bean.getCourseId());
		bean.setSubjectId(DataUtility.getLong(request.getParameter("subjectid")));
		bean.setCourseName(DataUtility.getString(request.getParameter("coursename")));
		bean.setCollegeName(DataUtility.getString(request.getParameter("collegename")));
		bean.setSubjectName(DataUtility.getString(request.getParameter("subjectname")));


		return bean;
	}

	/**
	 * Contains display logic.
	 *
	 * @param request  the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List list;
		List nextList = null;

		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

		FacultyModel model = new FacultyModel();
		FacultyBean bean = (FacultyBean) populateBean(request);

		String op = DataUtility.getString(request.getParameter("operation"));

		try {
			list = model.search(bean, pageNo, pageSize);

			nextList = model.search(bean, pageNo + 1, pageSize);

			request.setAttribute("nextlist", nextList.size());

			ServletUtility.setList(list, request);
			if (list == null || list.size() == 0) {
				ServletUtility.setErrorMessage("No record found ", request);
			}
			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			ServletUtility.forward(getView(), request, response);

		} catch (ApplicationException e) {
			e.printStackTrace();
			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		}

		log.debug(" DoGet Method of Faculty Model End");
	}

	/**
	 * Contains submit logic.
	 *
	 * @param request  the request
	 * @param response the response
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List list;
		List nextList = null;

		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

		String op = DataUtility.getString(request.getParameter("operation"));

		FacultyBean bean = (FacultyBean) populateBean(request);
		FacultyModel model = new FacultyModel();

		String[] ids = (String[]) request.getParameterValues("ids");

		if (OP_SEARCH.equalsIgnoreCase(op)) {
			pageNo = 1;
		} else if (OP_NEXT.equalsIgnoreCase(op)) {
			pageNo++;
		} else if (OP_PREVIOUS.equalsIgnoreCase(op)) {
			if (pageNo > 1) {
				pageNo--;
			} else {
				pageNo = 1;
			}
		} else if (OP_NEW.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.FACULTY_CTL, request, response);
			return;
		} else if (OP_RESET.equalsIgnoreCase(op) || OP_BACK.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.FACULTY_LIST_CTL, request, response);
			return;
		}

		else if (OP_DELETE.equalsIgnoreCase(op)) {
			pageNo = 1;
			if (ids != null && ids.length != 0) {
				FacultyBean deletebean = new FacultyBean();
				for (String id : ids) {
					deletebean.setId(DataUtility.getInt(id));
					try {
						model.delete(deletebean);
					} catch (ApplicationException e) {
						e.printStackTrace();
						log.error(e);
						ServletUtility.handleException(e, request, response);
						return;
					}
					ServletUtility.setSuccessMessage("Data Deleted Succesfully", request);
				}

			} else {
				ServletUtility.setErrorMessage("Select at least one record", request);
			}
		}
		try {
			list = model.search(bean, pageNo, pageSize);

			nextList = model.search(bean, pageNo + 1, pageSize);

			request.setAttribute("nextlist", nextList.size());

			ServletUtility.setBean(bean, request);
		} catch (ApplicationException e) {
			e.printStackTrace();
			ServletUtility.handleException(e, request, response);
			return;
		}

		if (list == null || list.size() == 0 && !OP_DELETE.equalsIgnoreCase(op)) {
			ServletUtility.setErrorMessage("No Record Found", request);
		}

		ServletUtility.setList(list, request);
		ServletUtility.setBean(bean, request);
		ServletUtility.setPageNo(pageNo, request);
		ServletUtility.setPageSize(pageSize, request);
		ServletUtility.forward(getView(), request, response);

		// System.out.println("===faculty list ctl===" + list.size() + list + op);
		log.debug("UserListCtl doPost End");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see in.co.pro4.controller.BaseCtl#getview()
	 */
	@Override
	protected String getView() {
		return ORSView.FACULTY_LIST_VIEW;
	}
}
