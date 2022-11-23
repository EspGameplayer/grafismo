import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IReferee, Referee } from '../referee.model';

import { RefereeService } from './referee.service';

describe('Referee Service', () => {
  let service: RefereeService;
  let httpMock: HttpTestingController;
  let elemDefault: IReferee;
  let expectedResult: IReferee | IReferee[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RefereeService);
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

    it('should create a Referee', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Referee()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Referee', () => {
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

    it('should partial update a Referee', () => {
      const patchObject = Object.assign({}, new Referee());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Referee', () => {
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

    it('should delete a Referee', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRefereeToCollectionIfMissing', () => {
      it('should add a Referee to an empty array', () => {
        const referee: IReferee = { id: 123 };
        expectedResult = service.addRefereeToCollectionIfMissing([], referee);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(referee);
      });

      it('should not add a Referee to an array that contains it', () => {
        const referee: IReferee = { id: 123 };
        const refereeCollection: IReferee[] = [
          {
            ...referee,
          },
          { id: 456 },
        ];
        expectedResult = service.addRefereeToCollectionIfMissing(refereeCollection, referee);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Referee to an array that doesn't contain it", () => {
        const referee: IReferee = { id: 123 };
        const refereeCollection: IReferee[] = [{ id: 456 }];
        expectedResult = service.addRefereeToCollectionIfMissing(refereeCollection, referee);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(referee);
      });

      it('should add only unique Referee to an array', () => {
        const refereeArray: IReferee[] = [{ id: 123 }, { id: 456 }, { id: 51244 }];
        const refereeCollection: IReferee[] = [{ id: 123 }];
        expectedResult = service.addRefereeToCollectionIfMissing(refereeCollection, ...refereeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const referee: IReferee = { id: 123 };
        const referee2: IReferee = { id: 456 };
        expectedResult = service.addRefereeToCollectionIfMissing([], referee, referee2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(referee);
        expect(expectedResult).toContain(referee2);
      });

      it('should accept null and undefined values', () => {
        const referee: IReferee = { id: 123 };
        expectedResult = service.addRefereeToCollectionIfMissing([], null, referee, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(referee);
      });

      it('should return initial array if no Referee is added', () => {
        const refereeCollection: IReferee[] = [{ id: 123 }];
        expectedResult = service.addRefereeToCollectionIfMissing(refereeCollection, undefined, null);
        expect(expectedResult).toEqual(refereeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
