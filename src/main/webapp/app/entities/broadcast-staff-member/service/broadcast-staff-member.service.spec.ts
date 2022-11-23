import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBroadcastStaffMember, BroadcastStaffMember } from '../broadcast-staff-member.model';

import { BroadcastStaffMemberService } from './broadcast-staff-member.service';

describe('BroadcastStaffMember Service', () => {
  let service: BroadcastStaffMemberService;
  let httpMock: HttpTestingController;
  let elemDefault: IBroadcastStaffMember;
  let expectedResult: IBroadcastStaffMember | IBroadcastStaffMember[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BroadcastStaffMemberService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
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

    it('should create a BroadcastStaffMember', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new BroadcastStaffMember()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BroadcastStaffMember', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a BroadcastStaffMember', () => {
      const patchObject = Object.assign({}, new BroadcastStaffMember());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BroadcastStaffMember', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
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

    it('should delete a BroadcastStaffMember', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addBroadcastStaffMemberToCollectionIfMissing', () => {
      it('should add a BroadcastStaffMember to an empty array', () => {
        const broadcastStaffMember: IBroadcastStaffMember = { id: 123 };
        expectedResult = service.addBroadcastStaffMemberToCollectionIfMissing([], broadcastStaffMember);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(broadcastStaffMember);
      });

      it('should not add a BroadcastStaffMember to an array that contains it', () => {
        const broadcastStaffMember: IBroadcastStaffMember = { id: 123 };
        const broadcastStaffMemberCollection: IBroadcastStaffMember[] = [
          {
            ...broadcastStaffMember,
          },
          { id: 456 },
        ];
        expectedResult = service.addBroadcastStaffMemberToCollectionIfMissing(broadcastStaffMemberCollection, broadcastStaffMember);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BroadcastStaffMember to an array that doesn't contain it", () => {
        const broadcastStaffMember: IBroadcastStaffMember = { id: 123 };
        const broadcastStaffMemberCollection: IBroadcastStaffMember[] = [{ id: 456 }];
        expectedResult = service.addBroadcastStaffMemberToCollectionIfMissing(broadcastStaffMemberCollection, broadcastStaffMember);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(broadcastStaffMember);
      });

      it('should add only unique BroadcastStaffMember to an array', () => {
        const broadcastStaffMemberArray: IBroadcastStaffMember[] = [{ id: 123 }, { id: 456 }, { id: 56169 }];
        const broadcastStaffMemberCollection: IBroadcastStaffMember[] = [{ id: 123 }];
        expectedResult = service.addBroadcastStaffMemberToCollectionIfMissing(broadcastStaffMemberCollection, ...broadcastStaffMemberArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const broadcastStaffMember: IBroadcastStaffMember = { id: 123 };
        const broadcastStaffMember2: IBroadcastStaffMember = { id: 456 };
        expectedResult = service.addBroadcastStaffMemberToCollectionIfMissing([], broadcastStaffMember, broadcastStaffMember2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(broadcastStaffMember);
        expect(expectedResult).toContain(broadcastStaffMember2);
      });

      it('should accept null and undefined values', () => {
        const broadcastStaffMember: IBroadcastStaffMember = { id: 123 };
        expectedResult = service.addBroadcastStaffMemberToCollectionIfMissing([], null, broadcastStaffMember, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(broadcastStaffMember);
      });

      it('should return initial array if no BroadcastStaffMember is added', () => {
        const broadcastStaffMemberCollection: IBroadcastStaffMember[] = [{ id: 123 }];
        expectedResult = service.addBroadcastStaffMemberToCollectionIfMissing(broadcastStaffMemberCollection, undefined, null);
        expect(expectedResult).toEqual(broadcastStaffMemberCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
