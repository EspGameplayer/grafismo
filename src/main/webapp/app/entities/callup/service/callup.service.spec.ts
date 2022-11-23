import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICallup, Callup } from '../callup.model';

import { CallupService } from './callup.service';

describe('Callup Service', () => {
  let service: CallupService;
  let httpMock: HttpTestingController;
  let elemDefault: ICallup;
  let expectedResult: ICallup | ICallup[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CallupService);
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

    it('should create a Callup', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Callup()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Callup', () => {
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

    it('should partial update a Callup', () => {
      const patchObject = Object.assign({}, new Callup());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Callup', () => {
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

    it('should delete a Callup', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCallupToCollectionIfMissing', () => {
      it('should add a Callup to an empty array', () => {
        const callup: ICallup = { id: 123 };
        expectedResult = service.addCallupToCollectionIfMissing([], callup);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(callup);
      });

      it('should not add a Callup to an array that contains it', () => {
        const callup: ICallup = { id: 123 };
        const callupCollection: ICallup[] = [
          {
            ...callup,
          },
          { id: 456 },
        ];
        expectedResult = service.addCallupToCollectionIfMissing(callupCollection, callup);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Callup to an array that doesn't contain it", () => {
        const callup: ICallup = { id: 123 };
        const callupCollection: ICallup[] = [{ id: 456 }];
        expectedResult = service.addCallupToCollectionIfMissing(callupCollection, callup);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(callup);
      });

      it('should add only unique Callup to an array', () => {
        const callupArray: ICallup[] = [{ id: 123 }, { id: 456 }, { id: 44415 }];
        const callupCollection: ICallup[] = [{ id: 123 }];
        expectedResult = service.addCallupToCollectionIfMissing(callupCollection, ...callupArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const callup: ICallup = { id: 123 };
        const callup2: ICallup = { id: 456 };
        expectedResult = service.addCallupToCollectionIfMissing([], callup, callup2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(callup);
        expect(expectedResult).toContain(callup2);
      });

      it('should accept null and undefined values', () => {
        const callup: ICallup = { id: 123 };
        expectedResult = service.addCallupToCollectionIfMissing([], null, callup, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(callup);
      });

      it('should return initial array if no Callup is added', () => {
        const callupCollection: ICallup[] = [{ id: 123 }];
        expectedResult = service.addCallupToCollectionIfMissing(callupCollection, undefined, null);
        expect(expectedResult).toEqual(callupCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
