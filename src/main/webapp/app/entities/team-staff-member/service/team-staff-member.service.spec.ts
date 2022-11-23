import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { StaffMemberRole } from 'app/entities/enumerations/staff-member-role.model';
import { ITeamStaffMember, TeamStaffMember } from '../team-staff-member.model';

import { TeamStaffMemberService } from './team-staff-member.service';

describe('TeamStaffMember Service', () => {
  let service: TeamStaffMemberService;
  let httpMock: HttpTestingController;
  let elemDefault: ITeamStaffMember;
  let expectedResult: ITeamStaffMember | ITeamStaffMember[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TeamStaffMemberService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      role: StaffMemberRole.DT,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a TeamStaffMember', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new TeamStaffMember()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TeamStaffMember', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          role: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TeamStaffMember', () => {
      const patchObject = Object.assign(
        {
          role: 'BBBBBB',
        },
        new TeamStaffMember()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TeamStaffMember', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          role: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a TeamStaffMember', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTeamStaffMemberToCollectionIfMissing', () => {
      it('should add a TeamStaffMember to an empty array', () => {
        const teamStaffMember: ITeamStaffMember = { id: 123 };
        expectedResult = service.addTeamStaffMemberToCollectionIfMissing([], teamStaffMember);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(teamStaffMember);
      });

      it('should not add a TeamStaffMember to an array that contains it', () => {
        const teamStaffMember: ITeamStaffMember = { id: 123 };
        const teamStaffMemberCollection: ITeamStaffMember[] = [
          {
            ...teamStaffMember,
          },
          { id: 456 },
        ];
        expectedResult = service.addTeamStaffMemberToCollectionIfMissing(teamStaffMemberCollection, teamStaffMember);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TeamStaffMember to an array that doesn't contain it", () => {
        const teamStaffMember: ITeamStaffMember = { id: 123 };
        const teamStaffMemberCollection: ITeamStaffMember[] = [{ id: 456 }];
        expectedResult = service.addTeamStaffMemberToCollectionIfMissing(teamStaffMemberCollection, teamStaffMember);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(teamStaffMember);
      });

      it('should add only unique TeamStaffMember to an array', () => {
        const teamStaffMemberArray: ITeamStaffMember[] = [{ id: 123 }, { id: 456 }, { id: 58212 }];
        const teamStaffMemberCollection: ITeamStaffMember[] = [{ id: 123 }];
        expectedResult = service.addTeamStaffMemberToCollectionIfMissing(teamStaffMemberCollection, ...teamStaffMemberArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const teamStaffMember: ITeamStaffMember = { id: 123 };
        const teamStaffMember2: ITeamStaffMember = { id: 456 };
        expectedResult = service.addTeamStaffMemberToCollectionIfMissing([], teamStaffMember, teamStaffMember2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(teamStaffMember);
        expect(expectedResult).toContain(teamStaffMember2);
      });

      it('should accept null and undefined values', () => {
        const teamStaffMember: ITeamStaffMember = { id: 123 };
        expectedResult = service.addTeamStaffMemberToCollectionIfMissing([], null, teamStaffMember, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(teamStaffMember);
      });

      it('should return initial array if no TeamStaffMember is added', () => {
        const teamStaffMemberCollection: ITeamStaffMember[] = [{ id: 123 }];
        expectedResult = service.addTeamStaffMemberToCollectionIfMissing(teamStaffMemberCollection, undefined, null);
        expect(expectedResult).toEqual(teamStaffMemberCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
