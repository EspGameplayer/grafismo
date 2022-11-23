import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILocalAssociationRegion, LocalAssociationRegion } from '../local-association-region.model';

import { LocalAssociationRegionService } from './local-association-region.service';

describe('LocalAssociationRegion Service', () => {
  let service: LocalAssociationRegionService;
  let httpMock: HttpTestingController;
  let elemDefault: ILocalAssociationRegion;
  let expectedResult: ILocalAssociationRegion | ILocalAssociationRegion[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LocalAssociationRegionService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
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

    it('should create a LocalAssociationRegion', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new LocalAssociationRegion()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LocalAssociationRegion', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a LocalAssociationRegion', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
        },
        new LocalAssociationRegion()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of LocalAssociationRegion', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
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

    it('should delete a LocalAssociationRegion', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addLocalAssociationRegionToCollectionIfMissing', () => {
      it('should add a LocalAssociationRegion to an empty array', () => {
        const localAssociationRegion: ILocalAssociationRegion = { id: 123 };
        expectedResult = service.addLocalAssociationRegionToCollectionIfMissing([], localAssociationRegion);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(localAssociationRegion);
      });

      it('should not add a LocalAssociationRegion to an array that contains it', () => {
        const localAssociationRegion: ILocalAssociationRegion = { id: 123 };
        const localAssociationRegionCollection: ILocalAssociationRegion[] = [
          {
            ...localAssociationRegion,
          },
          { id: 456 },
        ];
        expectedResult = service.addLocalAssociationRegionToCollectionIfMissing(localAssociationRegionCollection, localAssociationRegion);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LocalAssociationRegion to an array that doesn't contain it", () => {
        const localAssociationRegion: ILocalAssociationRegion = { id: 123 };
        const localAssociationRegionCollection: ILocalAssociationRegion[] = [{ id: 456 }];
        expectedResult = service.addLocalAssociationRegionToCollectionIfMissing(localAssociationRegionCollection, localAssociationRegion);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(localAssociationRegion);
      });

      it('should add only unique LocalAssociationRegion to an array', () => {
        const localAssociationRegionArray: ILocalAssociationRegion[] = [{ id: 123 }, { id: 456 }, { id: 19855 }];
        const localAssociationRegionCollection: ILocalAssociationRegion[] = [{ id: 123 }];
        expectedResult = service.addLocalAssociationRegionToCollectionIfMissing(
          localAssociationRegionCollection,
          ...localAssociationRegionArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const localAssociationRegion: ILocalAssociationRegion = { id: 123 };
        const localAssociationRegion2: ILocalAssociationRegion = { id: 456 };
        expectedResult = service.addLocalAssociationRegionToCollectionIfMissing([], localAssociationRegion, localAssociationRegion2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(localAssociationRegion);
        expect(expectedResult).toContain(localAssociationRegion2);
      });

      it('should accept null and undefined values', () => {
        const localAssociationRegion: ILocalAssociationRegion = { id: 123 };
        expectedResult = service.addLocalAssociationRegionToCollectionIfMissing([], null, localAssociationRegion, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(localAssociationRegion);
      });

      it('should return initial array if no LocalAssociationRegion is added', () => {
        const localAssociationRegionCollection: ILocalAssociationRegion[] = [{ id: 123 }];
        expectedResult = service.addLocalAssociationRegionToCollectionIfMissing(localAssociationRegionCollection, undefined, null);
        expect(expectedResult).toEqual(localAssociationRegionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
