public class MemberServiceImpl {
    private MemberDAOImpl memberDAO;

    public MemberDAOImpl getMemberDAO() {
        return memberDAO;
    }

    public void setMemberDAO(MemberDAOImpl memberDAO) {
        this.memberDAO = memberDAO;
    }

    public void add(){
        System.out.println("MemberSeverImp add() 被调用");
        memberDAO.add();
    }
}
